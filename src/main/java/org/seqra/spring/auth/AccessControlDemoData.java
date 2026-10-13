package org.seqra.spring.auth;

import org.seqra.spring.persistence.UserNote;
import org.seqra.spring.persistence.UserNoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessControlDemoData {

    @Bean
    CommandLineRunner seedAccessControlNotes(UserNoteRepository notes) {
        return args -> {
            if (notes.count() == 0) {
                notes.save(new UserNote("Alice's note", "Alice's private content", "alice@example.test"));
                notes.save(new UserNote("Bob's note", "Bob's private content", "bob@example.test"));
            }
        };
    }
}
