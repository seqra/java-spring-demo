package org.seqra.spring.auth;

import java.util.List;
import org.seqra.spring.persistence.UserNote;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

// Safe at runtime, but these handlers deliberately violate the annotation standard.
@RestController
@RequestMapping("/api/access-control/nonconforming/notes")
public class NonconformingNoteController {
    private final NoteAccessService notes;
    private final NoteAuthorization authorization;

    public NonconformingNoteController(NoteAccessService notes, NoteAuthorization authorization) {
        this.notes = notes;
        this.authorization = authorization;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public UserNote read(@PathVariable("id") long id, Authentication authentication) {
        if (!authorization.isOwner(id, authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return notes.readById(id);
    }

    @GetMapping
    @PreAuthorize("#ownerEmail == authentication.name")
    public List<UserNote> list(@P("ownerEmail") @RequestParam("ownerEmail") String ownerEmail) {
        return notes.listByOwner(ownerEmail);
    }
}
