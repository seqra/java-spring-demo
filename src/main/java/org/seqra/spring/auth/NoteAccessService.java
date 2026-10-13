package org.seqra.spring.auth;

import java.util.List;
import org.seqra.spring.persistence.UserNote;
import org.seqra.spring.persistence.UserNoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NoteAccessService {
    private final UserNoteRepository notes;

    public NoteAccessService(UserNoteRepository notes) {
        this.notes = notes;
    }

    public UserNote readById(long id) {
        return notes.findById(id).orElseThrow(NoteAccessService::notFound);
    }

    @Transactional
    public void deleteById(long id) {
        notes.delete(readById(id));
    }

    public List<UserNote> listByOwner(String ownerEmail) {
        return notes.findByOwnerEmail(ownerEmail);
    }

    public UserNote readOwnedById(long id, String ownerEmail) {
        return notes.findByIdAndOwnerEmail(id, ownerEmail).orElseThrow(NoteAccessService::notFound);
    }

    @Transactional
    public void deleteOwnedById(long id, String ownerEmail) {
        notes.delete(readOwnedById(id, ownerEmail));
    }

    private static ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
