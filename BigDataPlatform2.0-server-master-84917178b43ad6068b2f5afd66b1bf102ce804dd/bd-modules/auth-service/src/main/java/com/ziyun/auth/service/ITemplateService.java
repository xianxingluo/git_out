package com.ziyun.auth.service;

import com.github.pagehelper.PageInfo;
import com.ziyun.auth.model.Template;

/**
 * @author leyangjie
 * @Description: ${todo}
 * @date 2018/4/27 10:17
 */
public interface ITemplateService extends IService<Template> {
    void deleteTemplate(Template template);

    PageInfo<Template> listTemplates(Template template);

    void saveTemplate(Template template);

    void updateTemplate(Template template);

    int listTemplatesCount(Template template);

    int isExistTemplate(String tempplateName);
}
