package com.ziyun.auth.controller;

import com.github.pagehelper.PageInfo;
import com.ziyun.auth.model.Template;
import com.ziyun.auth.service.ITemplateService;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leyangjie
 * @Description: 模板controller
 * @date 2018/4/26 15:19
 */
@RestController
@RequestMapping("/api/v2/auth")
@Api(tags = "模板模块", description = "模板模块相关api")
public class TemplateController {

    @Autowired
    private ITemplateService templateService;

    /**
     * 添加模板
     */
    @ApiOperation(value = "添加模板", notes = "添加模板")
    @RequestMapping(value = "/template/save", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateName", dataType = "String", required = true, value = "模板名"),
            @ApiImplicitParam(paramType = "query", name = "createUserId", dataType = "Long", required = true, value = "创建模板的userid"),
            @ApiImplicitParam(paramType = "query", name = "remark", dataType = "String", required = true, value = "备注"),
            @ApiImplicitParam(paramType = "query", name = "roleId", dataType = "Long", required = true, value = "角色id"),
            @ApiImplicitParam(paramType = "query", name = "funcPermIds", dataType = "Long[]", required = true, value = "可存放多个功能权限id")

    })
    public CommonResponse addTemplate(Template template) {
        try {
            templateService.saveTemplate(template);
            return CommonResponse.success("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure("操作失败！");
        }
    }

    /**
     * 删除模板
     */
    @RequestMapping(value = "/template/remove", method = RequestMethod.POST)
    @ApiOperation(value = "删除模板", notes = "删除模板")
    @ApiImplicitParam(paramType = "query", name = "ids", dataType = "Long[]", required = true, value = "可存放多个模板id，支持批量删除")
    public CommonResponse removeTemplate(Template template) {
        try {
            templateService.deleteTemplate(template);
            return CommonResponse.success("1");
        } catch (Exception e) {
            return CommonResponse.failure("0");
        }
    }

    /**
     * 查询模板
     */
    @ApiOperation(value = "查询模板", notes = "根据模板名，查询模板列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateName", dataType = "String", value = "根据模板名，支持模糊查询模板列表"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "Integer", value = "当前页数"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", value = "每页显示的条数")}
    )
    @RequestMapping(value = "/templates", method = RequestMethod.GET)
    public CommonResponse listTemplates(Template template) {
        PageInfo<Template> templatePageInfo = templateService.listTemplates(template);
        List<Template> list = templatePageInfo.getList();
        Map<String, Object> map = new HashMap<>(Constant.INT_FOUR);
        map.put("list", list);
        map.put("total", templatePageInfo.getTotal());
        return CommonResponse.success(map);
    }


    /**
     * 修改模板
     */
    @RequestMapping(value = "/template/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改模板", notes = "根据模板id，修改模板")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Long", required = true, value = "根据模板id，修改模板"),
            @ApiImplicitParam(paramType = "query", name = "templateName", dataType = "String", value = "模板名"),
            @ApiImplicitParam(paramType = "query", name = "remark", dataType = "String", value = "备注"),
            @ApiImplicitParam(paramType = "query", name = "roleId", dataType = "String", value = "备注"),
            @ApiImplicitParam(paramType = "query", name = "funcPermIds", dataType = "Long[]", required = true, value = "可存放多个功能权限id")}
    )
    public CommonResponse updateTemplate(Template template) {
        try {
            templateService.updateTemplate(template);
            return CommonResponse.success("修改成功！");
        } catch (Exception e) {
            return CommonResponse.failure("修改失败");
        }
    }

    /**
     * 根据模板名，判断改模板是否已经存在
     */
    @RequestMapping(value = "/template/isExistTemplate", method = RequestMethod.GET)
    @ApiOperation(value = "根据模板名，判断改模板是否已经存在", notes = "根据模板名，判断改模板是否已经存在")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateName", dataType = "String", required = true, value = "根据模板id，修改模板"),
    }
    )
    public CommonResponse isExistTemplate(String templateName) {
        int size = templateService.isExistTemplate(templateName);
        return CommonResponse.success(size);
    }


}
