package org.seqra.spring.messaging;

import java.util.Optional;

import org.seqra.spring.persistence.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Creates a new message with user-provided content.
     * Data flows from request body fields to database storage.
     * This is the source endpoint for cross-endpoint data flow.
     */
    @PostMapping
    public ResponseEntity<Long> createMessage(@RequestBody CreateMessageRequest request) {
        Message message = messageService.createMessage(
                request.getTitle(),
                request.getContent(),
                request.getAuthor()
        );
        return ResponseEntity.ok(message.getId());
    }

    /**
     * Retrieves message content and returns it as HTML.
     * Cross-endpoint data flow: stored user content flows to response.
     * Column-sensitive: only the content field is returned.
     */
    @GetMapping("/{id}/content")
    public ResponseEntity<String> getMessageContent(@PathVariable Long id) {
        Optional<Message> messageOpt = messageService.findById(id);
        if (messageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Message message = messageOpt.get();
        // Data flows from database (content column) directly to response - XSS sink
        String content = messageService.getMessageContent(message);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(content);
    }

    /**
     * Retrieves message content with HTML sanitization.
     * Cross-endpoint data flow with sanitizer applied.
     * HtmlUtils.htmlEscape() neutralizes XSS payloads.
     */
    @GetMapping("/{id}/content/safe")
    public ResponseEntity<String> getMessageContentSafe(@PathVariable Long id) {
        Optional<Message> messageOpt = messageService.findById(id);
        if (messageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Message message = messageOpt.get();
        // Data flows through HtmlUtils.htmlEscape() sanitizer
        String content = messageService.getMessageContentSafe(message);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(content);
    }

    /**
     * Retrieves message title - demonstrates column sensitivity.
     * Only the title column flows through, not content or author.
     */
    @GetMapping("/{id}/title")
    public ResponseEntity<String> getMessageTitle(@PathVariable Long id) {
        Optional<Message> messageOpt = messageService.findById(id);
        if (messageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Message message = messageOpt.get();
        // Column-sensitive: title field flows to response
        String title = message.getTitle();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(title);
    }

    /**
     * Retrieves message author - demonstrates column sensitivity.
     * Only the escaped author flows through, not content or title.
     */
    @GetMapping("/{id}/author")
    public ResponseEntity<String> getMessageAuthor(@PathVariable Long id) {
        Optional<Message> messageOpt = messageService.findById(id);
        if (messageOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Message message = messageOpt.get();
        // Column-sensitive: title field flows to response
        String author = message.getAuthor();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(author);
    }

    /**
     * Returns the last content stored via createMessage service field state.
     * Stored XSS: user content flows from service field to response without sanitization.
     * Data flow: request body (createMessage) -> service field (lastContent) -> response body.
     */
    @GetMapping("/last-content")
    public ResponseEntity<String> getLastContent() {
        String content = messageService.getLastContent();
        if (content == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(content);
    }
}
