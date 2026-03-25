package org.seqra.spring.content;

import freemarker.template.TemplateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Controller for campaign and notification template rendering.
 */
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final MarketingTemplateService marketingService;
    private final NotificationTemplateService notificationService;
    private final TemplateRenderingService templateService;

    public CampaignController(MarketingTemplateService marketingService,
                              NotificationTemplateService notificationService,
                              TemplateRenderingService templateService) {
        this.marketingService = marketingService;
        this.notificationService = notificationService;
        this.templateService = templateService;
    }

    /**
     * Preview a marketing email template before sending to customers.
     */
    @PostMapping("/marketing/preview")
    public ResponseEntity<String> previewMarketing(@RequestBody RenderRequest request) {
        try {
            String result = marketingService.render(request.getTemplateName(), request.getTemplateContent());
            return ResponseEntity.ok(result);
        } catch (IOException | TemplateException e) {
            return ResponseEntity.badRequest().body("Preview failed: " + e.getMessage());
        }
    }

    /**
     * Render a notification template with hardened configuration.
     */
    @PostMapping("/notifications/render")
    public ResponseEntity<String> renderNotification(@RequestBody RenderRequest request) {
        try {
            String result = notificationService.render(request.getTemplateName(), request.getTemplateContent());
            return ResponseEntity.ok(result);
        } catch (IOException | TemplateException e) {
            return ResponseEntity.badRequest().body("Render failed: " + e.getMessage());
        }
    }

    /**
     * Renders a custom template from user input.
     * Data flows from request body → DTO fields → service → template engine.
     */
    @PostMapping("/render")
    public ResponseEntity<String> renderTemplate(@RequestBody RenderRequest request) {
        try {
            String result = templateService.renderFromRequest(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Template processing failed: " + e.getMessage());
        }
    }

    /**
     * Alternative endpoint demonstrating data flow through object field assignment.
     */
    @PostMapping("/render/inline")
    public ResponseEntity<String> renderInline(@RequestBody RenderRequest request) {
        try {
            String templateContent = request.getTemplateContent();
            String result = templateService.renderFromContent(templateContent);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Processing failed: " + e.getMessage());
        }
    }
}
