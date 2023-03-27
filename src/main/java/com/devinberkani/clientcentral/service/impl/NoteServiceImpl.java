package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.Note;
import com.devinberkani.clientcentral.mapper.ClientMapper;
import com.devinberkani.clientcentral.mapper.NoteMapper;
import com.devinberkani.clientcentral.repository.ClientRepository;
import com.devinberkani.clientcentral.repository.NoteRepository;
import com.devinberkani.clientcentral.service.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

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

    // handle get page of notes based on client, page number, and sort direction
    @Override
    public Page<NoteDto> getNotesPage(ClientDto clientDto, int pageNo, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by("createdOn").ascending() : Sort.by("createdOn").descending();
        Pageable pageable = PageRequest.of(pageNo - 1, 5, sort);
        Client client = ClientMapper.mapToClient(clientDto);
        // FIXME: AFTER SPRING SECURITY - below hardcoded user id (1) to find list of clients for dashboard - should get current logged in user
        return noteRepository.findByClient(client, pageable).map(NoteMapper::mapToNoteDto);
    }

    // handle create new note
    @Override
    public Long saveNewNote(NoteDto note, Long clientId) {
        Note newNote = NoteMapper.mapToNote(note);
        // set client w/ client id as owner for note
        newNote.setClient(clientRepository.findClientById(clientId));
        // save note to repository
        noteRepository.save(newNote);
        return newNote.getId();
    }

    // handle deleting note and corresponding files from the filesystem
    @Override
    public void deleteNote(Long noteId, Long clientId) {

        // handle deleting corresponding files from the filesystem

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

    // handle find note by id
    @Override
    public NoteDto findNoteById(Long noteId) {
        Note note = noteRepository.findNoteById(noteId);
        return NoteMapper.mapToNoteDto(note);
    }

    // handle updating a note
    @Override
    public void updateNote(NoteDto note, Long noteId, Long clientId) {
        note.setId(noteId);
        Note updatedNote = NoteMapper.mapToNote(note);
        updatedNote.setClient(clientRepository.findClientById(clientId));
        noteRepository.save(updatedNote);
    }
}
