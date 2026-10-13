package org.seqra.spring.auth;

import java.util.List;
import org.seqra.spring.persistence.UserNote;
import org.seqra.spring.persistence.UserNoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

// Reviewed contract: authorize the final selector and return immutable data.
@Service
public class NoteGateway {
    private final UserNoteRepository notes;

    public NoteGateway(UserNoteRepository notes) {
        this.notes = notes;
    }

    public record NoteView(long id, String subject, String body) {
        static NoteView from(UserNote note) {
            return new NoteView(note.getId(), note.getSubject(), note.getBody());
        }
    }

    public NoteView read(long id) {
        return notes.findByIdAndOwnerEmail(id, owner())
                .map(NoteView::from).orElseThrow(NoteGateway::notFound);
    }

    public List<NoteView> list() {
        return notes.findByOwnerEmail(owner()).stream().map(NoteView::from).toList();
    }

    @Transactional
    public void delete(long id) {
        if (notes.deleteOwned(id, owner()) == 0) {
            throw notFound();
        }
    }

    private static String owner() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return authentication.getName();
    }

    private static ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
