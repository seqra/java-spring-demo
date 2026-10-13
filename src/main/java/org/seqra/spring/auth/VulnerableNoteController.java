package org.seqra.spring.auth;

import java.util.List;
import org.seqra.spring.persistence.UserNote;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

// Authenticated users can select another user's notes.
@RestController
@RequestMapping("/api/access-control/vulnerable/notes")
public class VulnerableNoteController {
    private final NoteAccessService notes;

    public VulnerableNoteController(NoteAccessService notes) {
        this.notes = notes;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public UserNote read(@PathVariable("id") long id) {
        return notes.readById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public void delete(@PathVariable("id") long id) {
        notes.deleteById(id);
    }

    @GetMapping
    public List<UserNote> list(@RequestParam("ownerEmail") String ownerEmail) {
        return notes.listByOwner(ownerEmail);
    }

    @GetMapping("/boxed")
    public UserNote boxedId(@RequestParam("id") Long id) {
        return notes.readById(id);
    }

    @PostMapping("/body")
    public UserNote bodyId(@RequestBody Selection selection) {
        return notes.readById(selection.id());
    }

    @GetMapping("/forged-owner/{id}")
    public UserNote forgedOwner(@PathVariable("id") long id,
            @RequestParam("ownerEmail") String ownerEmail) {
        return notes.readOwnedById(id, ownerEmail);
    }

    @GetMapping("/wrong-guard/{id}")
    @PreAuthorize("@noteAuthorization.isOwner(#id, authentication)")
    public UserNote wrongGuard(@P("id") @PathVariable("id") long id,
            @RequestParam("otherId") long otherId) {
        return notes.readById(otherId);
    }

    @GetMapping("/weak-guard/{id}")
    @PreAuthorize("@noteAuthorization.isOwner(#id, authentication) or isAuthenticated()")
    public UserNote weakGuard(@P("id") @PathVariable("id") long id) {
        return notes.readById(id);
    }

    public record Selection(long id) {}
}
