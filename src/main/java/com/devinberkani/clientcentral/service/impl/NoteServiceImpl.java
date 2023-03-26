package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.entity.Note;
import com.devinberkani.clientcentral.mapper.NoteMapper;
import com.devinberkani.clientcentral.repository.ClientRepository;
import com.devinberkani.clientcentral.repository.NoteRepository;
import com.devinberkani.clientcentral.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class NoteServiceImpl implements NoteService {

    ClientRepository clientRepository;
    NoteRepository noteRepository;

    public NoteServiceImpl(ClientRepository clientRepository, NoteRepository noteRepository) {
        this.clientRepository = clientRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    public Long saveNewNote(NoteDto note, Long clientId) {
        Note newNote = NoteMapper.mapToNote(note);
        // set client w/ client id as owner for note
        newNote.setClient(clientRepository.findClientById(clientId));
        // save note to repository
        noteRepository.save(newNote);
        return newNote.getId();
    }

    @Override
    public void deleteNote(Long noteId, Long clientId) {

        // handle deleting corresponding files from the filesystem if a note is deleted

        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to set filepath for any existing files - should get current logged in user
        Path deletePath = Paths.get("src/main/resources/static/file-attachments/user-" + 1 + "/client-" + clientId + "/note-" + noteId);
        if (Files.exists(deletePath)) {
            try {
                FileSystemUtils.deleteRecursively(deletePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        noteRepository.deleteById(noteId);
    }
}
