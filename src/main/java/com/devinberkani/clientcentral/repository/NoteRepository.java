package com.devinberkani.clientcentral.repository;

import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("SELECT n FROM Note n JOIN n.client c WHERE c.user.id = :userId AND n.id = :noteId")
    Note findNoteByIdAndUser(@Param("noteId") Long noteId, @Param("userId") Long userId);
    @Query("SELECT n FROM Note n JOIN n.client c WHERE c = :client AND c.user.id = :userId")
    Page<Note> findByClientAndUser(@Param("client") Client client, @Param("userId") Long userId, Pageable page);

}
