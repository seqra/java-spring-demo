package org.seqra.spring.content

import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationTemplateService {

    private final Configuration templateConfig;

    public NotificationTemplateService() {
        Configuration templateConfig = new Configuration(Configuration.VERSION_2_3_33);
        templateConfig.setNewBuiltinClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);
        templateConfig.setAPIBuiltinEnabled(false);
        this.templateConfig = templateConfig;
    }

    public String render(String name, String content) throws IOException, TemplateException {
        Template template = new Template(name, new StringReader(content), templateConfig);

        Map<String, Object> model = new HashMap<>();
        model.put("appName", "Notification Service");
        model.put("templateId", name);

        StringWriter output = new StringWriter();
        template.process(model, output);
        return output.toString();
    }
}
