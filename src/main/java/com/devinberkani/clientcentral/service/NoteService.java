package com.devinberkani.clientcentral.service;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.dto.NoteDto;

import java.io.IOException;

public interface NoteService {

    Long saveNewNote(NoteDto note, Long clientId);
    void deleteNote(Long noteId, Long clientId);
    NoteDto findNoteById(Long noteId);
    void updateNote(NoteDto note, Long noteId, Long clientId);
}
