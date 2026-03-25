package org.seqra.spring.content;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Service
public class TemplateRenderingService {

    private final TemplateEngine templateEngine;

    public TemplateRenderingService() {
        this.templateEngine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        this.templateEngine.setTemplateResolver(resolver);
    }

    /**
     * Renders a template from user-provided content.
     * Data flows from the request through templateContent parameter to the TemplateEngine.
     */
    public String renderFromContent(String templateContent) {
        Context context = new Context();
        context.setVariable("appName", "Demo Application");

        return templateEngine.process(templateContent, context);
    }

    /**
     * Renders a template using the request object.
     * Demonstrates data flow through object field access.
     */
    public String renderFromRequest(RenderRequest request) {
        String content = request.getTemplateContent();
        return renderFromContent(content);
    }
}
