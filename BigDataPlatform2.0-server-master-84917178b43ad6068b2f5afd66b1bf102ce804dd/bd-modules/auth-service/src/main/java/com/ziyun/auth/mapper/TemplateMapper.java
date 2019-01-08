package com.ziyun.auth.mapper;

import com.ziyun.auth.model.Template;
import com.ziyun.auth.util.ZyMapper;

import java.util.List;

public interface TemplateMapper extends ZyMapper<Template> {
    void insertTemplateFuncPermission(Template template);

    void deleteTemplateAndPermis(Template template);

    void deleteTemplates(Template template);

    List<Template> listTemplates(Template template);

    void updateTemplateAndPermis(Template template);

    int listTemplatesCount(Template template);
}