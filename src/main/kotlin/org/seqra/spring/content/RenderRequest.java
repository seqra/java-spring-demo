package org.seqra.spring.content

/**
 * DTO for template rendering requests.
 * Data flows from request body through this object's fields.
 */
public class RenderRequest {
    private String templateContent;
    private String templateName;

    public RenderRequest() {
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
