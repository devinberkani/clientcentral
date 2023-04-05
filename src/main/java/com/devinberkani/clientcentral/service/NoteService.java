package com.devinberkani.clientcentral.service;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.dto.NoteDto;
import org.springframework.data.domain.Page;

import java.io.File;

public interface NoteService {

    Page<NoteDto> getNotesPage(ClientDto clientDto, int pageNo, String sortDir);
    Long saveNewNote(NoteDto note, Long clientId);
    int deleteNote(Long noteId, Long clientId);
    NoteDto findNoteById(Long noteId);
    void updateNote(NoteDto note, Long noteId, Long clientId);
    void deleteDirectoryIfEmpty(File directory);

}
