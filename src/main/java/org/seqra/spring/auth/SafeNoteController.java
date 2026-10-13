package org.seqra.spring.auth;

import java.util.List;
import org.seqra.spring.auth.NoteGateway.NoteView;
import org.seqra.spring.persistence.UserNote;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

// Policy: users may access only their own notes.
@RestController
@RequestMapping("/api/access-control/safe/notes")
public class SafeNoteController {
    private final NoteGateway notes;
    private final NoteAccessService rawNotes;

    public SafeNoteController(NoteGateway notes, NoteAccessService rawNotes) {
        this.notes = notes;
        this.rawNotes = rawNotes;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public NoteView read(@PathVariable("id") long id) {
        return notes.read(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void delete(@PathVariable("id") long id) {
        notes.delete(id);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<NoteView> list() {
        return notes.list();
    }

    @GetMapping("/guarded/{id}")
    @PreAuthorize("isAuthenticated() and @noteAuthorization.isOwner(#id, authentication)")
    public UserNote guardedRead(@P("id") @PathVariable("id") long id) {
        return rawNotes.readById(id);
    }

    @DeleteMapping("/guarded/{id}")
    @PreAuthorize("isAuthenticated() and @noteAuthorization.isOwner(#id, authentication)")
    public void guardedDelete(@P("id") @PathVariable("id") long id) {
        rawNotes.deleteById(id);
    }

    @GetMapping("/guarded")
    @PreAuthorize("isAuthenticated() and #ownerEmail == authentication.name")
    public List<UserNote> guardedList(@P("ownerEmail") @RequestParam("ownerEmail") String ownerEmail) {
        return rawNotes.listByOwner(ownerEmail);
    }
}
