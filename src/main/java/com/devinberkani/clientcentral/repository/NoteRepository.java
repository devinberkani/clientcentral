package com.devinberkani.clientcentral.repository;

import com.devinberkani.clientcentral.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
