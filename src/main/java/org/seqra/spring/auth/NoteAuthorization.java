package org.seqra.spring.auth;

import org.seqra.spring.persistence.UserNoteRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("noteAuthorization")
public class NoteAuthorization {
    private final UserNoteRepository notes;

    public NoteAuthorization(UserNoteRepository notes) {
        this.notes = notes;
    }

    public boolean isOwner(long id, Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && notes.existsByIdAndOwnerEmail(id, authentication.getName());
    }
}
