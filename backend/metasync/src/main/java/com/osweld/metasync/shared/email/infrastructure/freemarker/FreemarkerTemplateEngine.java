package com.osweld.metasync.shared.email.infrastructure.freemarker;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.osweld.metasync.shared.email.domain.port.TemplateEngine;

import freemarker.template.Configuration;

@Component
public class FreemarkerTemplateEngine implements TemplateEngine {

    private final Configuration freemarkerConfig;

    public FreemarkerTemplateEngine(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    @Override
    public String render(String templateName, Map<String, Object> model) {
        try {
            var template = freemarkerConfig.getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            throw new RuntimeException("Error processing email template", e);
        }
    }

}
