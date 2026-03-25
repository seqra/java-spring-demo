package org.seqra.spring.messaging;

import org.seqra.spring.persistence.Message;
import org.seqra.spring.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private String lastContent;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Creates and stores a new message with user-provided content.
     * Data flows from request parameters to database columns.
     * Also stores the content in service field state (lastContent).
     */
    public Message createMessage(String title, String content, String author) {
        this.lastContent = content;
        Message message = new Message(title, content, author);
        return messageRepository.save(message);
    }

    /**
     * Retrieves a message by ID.
     * Returns the stored data which may contain user-controlled content.
     */
    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    /**
     * Returns the raw content field from a stored message.
     * Column-sensitive: only the content field flows through this method.
     */
    public String getMessageContent(Message message) {
        return message.getContent();
    }

    /**
     * Returns the sanitized content field using HTML escaping.
     * HtmlUtils.htmlEscape() prevents XSS by encoding special characters.
     */
    public String getMessageContentSafe(Message message) {
        String content = message.getContent();
        return HtmlUtils.htmlEscape(content);
    }

    /**
     * Returns the last content stored via createMessage.
     * Data flows from service field state (lastContent) to the caller.
     * This is a stored XSS source through service field state.
     */
    public String getLastContent() {
        return lastContent;
    }
}
