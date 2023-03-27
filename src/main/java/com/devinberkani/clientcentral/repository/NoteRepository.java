package com.devinberkani.clientcentral.repository;

import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findNoteById(Long id);
    Page<Note> findByClient(Client client, Pageable page);
}
