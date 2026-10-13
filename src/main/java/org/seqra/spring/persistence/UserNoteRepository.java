package org.seqra.spring.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNoteRepository extends JpaRepository<UserNote, Long> {

    List<UserNote> findByOwnerEmail(String ownerEmail);

    Optional<UserNote> findByIdAndOwnerEmail(Long id, String ownerEmail);

    boolean existsByIdAndOwnerEmail(Long id, String ownerEmail);

    @Modifying
    @Query("delete from UserNote n where n.id = :id and n.ownerEmail = :owner")
    int deleteOwned(@Param("id") long id, @Param("owner") String owner);
}
