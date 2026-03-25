package org.seqra.spring.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNoteRepository extends JpaRepository<UserNote, Long> {

    List<UserNote> findByOwnerEmail(String ownerEmail);

    List<UserNote> findBySubjectContaining(String subjectPart);
}
