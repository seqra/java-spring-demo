package org.seqra.spring.content;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MarketingTemplateService {

    private final Configuration templateConfig;

    public MarketingTemplateService() {
        Configuration templateConfig = new Configuration(Configuration.VERSION_2_3_33);
        templateConfig.setNewBuiltinClassResolver(TemplateClassResolver.UNRESTRICTED_RESOLVER);
        templateConfig.setAPIBuiltinEnabled(true);
        this.templateConfig = templateConfig;
    }

    public String render(String name, String content) throws IOException, TemplateException {
        Template template = new Template(name, new StringReader(content), templateConfig);

        Map<String, Object> model = new HashMap<>();
        model.put("brandName", "Acme Corp");
        model.put("campaignId", name);

        StringWriter output = new StringWriter();
        template.process(model, output);
        return output.toString();
    }
}
