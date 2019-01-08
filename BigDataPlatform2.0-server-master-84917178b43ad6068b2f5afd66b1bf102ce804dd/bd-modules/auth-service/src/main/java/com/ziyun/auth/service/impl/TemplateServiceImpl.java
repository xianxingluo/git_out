package com.ziyun.auth.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ziyun.auth.mapper.FuncPermissionMapper;
import com.ziyun.auth.mapper.TemplateMapper;
import com.ziyun.auth.mapper.UserMapper;
import com.ziyun.auth.model.Template;
import com.ziyun.auth.service.ITemplateService;
import com.ziyun.common.entity.FuncPermission;
import com.ziyun.common.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author leyangjie
 * @Description: ${todo}
 * @date 2018/4/27 10:18
 */
@Service
@Transactional
public class TemplateServiceImpl extends BaseService<Template> implements ITemplateService {
    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FuncPermissionMapper funcPermissionMapper;

    /**
     * 删除模板
     *
     * @param template
     */
    @Override
    public void deleteTemplate(Template template) {
        //第一步：删除模板功能权限中间表（auth_permission_template）
        templateMapper.deleteTemplateAndPermis(template);
        //第二步：删除模板（支持批量）
        templateMapper.deleteTemplates(template);
    }

    /**
     * 查询模板:根据用户名模糊查询
     *
     * @param template
     * @return
     */
    @Override
    public PageInfo<Template> listTemplates(Template template) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNoneBlank(template.getTemplateName())) {
            criteria.andLike("templateName", "%" + template.getTemplateName() + "%");
        }
        if (template.getStart() != null) {
            PageHelper.startPage(template.getStart(), template.getLimit());
        }
        List<Template> templateList = selectByExample(example);
        //  List<Template> templateList = templateMapper.listTemplates(template);
        templateList.forEach(r -> {
            Long createUserId = r.getCreateUserId();
            //根据创建者主键id，查询创建者对象
            User user = null;// userMapper.selectByPrimaryKey(createUserId);
            //保留到模板对象中
            r.setCreateUser(user);
            List<FuncPermission> funcPermissions = funcPermissionMapper.listFuncPermissions(r.getId());
            r.setFuncPermissionList(funcPermissions);
        });

        return new PageInfo<>(templateList);
    }

    @Override
    public int listTemplatesCount(Template template) {
        return templateMapper.listTemplatesCount(template);
    }

    /**
     * 添加模板
     *
     * @param template
     * @return
     */
    @Override
    public void saveTemplate(Template template) {
        handleTemplatePortrait(template);
        //第一步：保存模板,对应(auth_template)
        templateMapper.insertUseGeneratedKeys(template);
        //第二步;保存功能权限中间表（auth_permission_template）
        templateMapper.insertTemplateFuncPermission(template);
    }

    /**
     * 修改模板
     *
     * @param template
     */
    @Override
    public void updateTemplate(Template template) {
        //第一步：修改模板功能权限中间表（auth_permission_template）,实际操作，先删除，再添加
        if (template != null && template.getFuncPermIds() != null && template.getFuncPermIds().length > 0) {
            handleTemplatePortrait(template);
            templateMapper.deleteTemplateAndPermis(template);
            templateMapper.insertTemplateFuncPermission(template);
        }
        //修改模板（auth_template）
        templateMapper.updateByPrimaryKey(template);
    }

    /**
     * 根据模板名，判断模板是否存在，返回结果为1，表示模板已经存在
     *
     * @param tempplateName
     * @return
     */
    @Override
    public int isExistTemplate(String tempplateName) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateName", tempplateName);
        List<Template> templates = templateMapper.selectByExample(example);
        if (templates != null && templates.size() > 0) {
            return 1;
        }
        return 0;
    }

    private void handleTemplatePortrait(Template template) {
        if (null != template) {
            //只要添加 "个人画像-*" 等子权限，默认添加个人画像（id:631  perms:stu_portrait）权限
            List<Long> portraitList = Arrays.asList(757L, 758L, 759L, 771L, 774L, 778L);
            Long[] funcPermIds = template.getFuncPermIds();
            List<Long> permIdList = new ArrayList<>(Arrays.asList(funcPermIds));
            for (Long perm : funcPermIds) {
                if (portraitList.contains(perm)) {
                    permIdList.add(631L);
                    template.setFuncPermIds(permIdList.toArray(new Long[permIdList.size()]));
                    return;
                }
            }
        }
    }
}
