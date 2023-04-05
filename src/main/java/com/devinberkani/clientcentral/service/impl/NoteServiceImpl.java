package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.Note;
import com.devinberkani.clientcentral.entity.User;
import com.devinberkani.clientcentral.mapper.ClientMapper;
import com.devinberkani.clientcentral.mapper.NoteMapper;
import com.devinberkani.clientcentral.repository.ClientRepository;
import com.devinberkani.clientcentral.repository.FileAttachmentRepository;
import com.devinberkani.clientcentral.repository.NoteRepository;
import com.devinberkani.clientcentral.service.NoteService;
import com.devinberkani.clientcentral.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    UserService userService;
    FileAttachmentRepository fileAttachmentRepository;

    public NoteServiceImpl(ClientRepository clientRepository, NoteRepository noteRepository, UserService userService, FileAttachmentRepository fileAttachmentRepository) {
        this.clientRepository = clientRepository;
        this.noteRepository = noteRepository;
        this.userService = userService;
        this.fileAttachmentRepository = fileAttachmentRepository;
    }

    // handle get page of notes based on client, page number, and sort direction
    @Override
    public Page<NoteDto> getNotesPage(ClientDto clientDto, int pageNo, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by("createdOn").ascending() : Sort.by("createdOn").descending();
        Pageable pageable = PageRequest.of(pageNo - 1, 5, sort);
        Client client = ClientMapper.mapToClient(clientDto);
        // get logged in user id
        Long userId = userService.getCurrentUser().getId();
        return noteRepository.findByClientAndUser(client, userId, pageable).map(NoteMapper::mapToNoteDto);
    }

    // handle create new note
    @Override
    public Long saveNewNote(NoteDto note, Long clientId) {
        Note newNote = NoteMapper.mapToNote(note);
        // get logged in user
        User user = userService.getCurrentUser();
        // set client w/ client id as owner for note
        newNote.setClient(clientRepository.findClientByIdAndUser(clientId, user));
        // save note to repository
        noteRepository.save(newNote);
        return newNote.getId();
    }

    // handle deleting note and corresponding files from the filesystem
    @Override
    public int deleteNote(Long noteId, Long clientId) {

        // get logged in user id
        Long currentUserId = userService.getCurrentUser().getId();

        // delete corresponding files (custom deleteNoteByIdAndUser method below implements a query that seems to impact CascadeType.REMOVE attribute in entity, so just doing it manually will suffice)
        fileAttachmentRepository.deleteFileAttachmentByNoteId(noteId);

        // delete note
        int deleted = noteRepository.deleteNoteByIdAndUser(noteId, currentUserId);

        if (deleted > 0) {

            // handle deleting corresponding files from the filesystem
            Path deletePath = Paths.get("src/main/resources/static/file-attachments/user-" + currentUserId + "/client-" + clientId + "/note-" + noteId);
            if (Files.exists(deletePath)) {

                try {
                    FileSystemUtils.deleteRecursively(deletePath);

                    // check if the specific client directory is empty, if it is, delete it from the filesystem
                    File clientDirectory = new File("src/main/resources/static/file-attachments/user-" + currentUserId + "/client-" + clientId);
                    deleteDirectoryIfEmpty(clientDirectory);

                    // check if the specific user directory is empty, if it is, delete it from the filesystem
                    File userDirectory = new File("src/main/resources/static/file-attachments/user-" + currentUserId);
                    deleteDirectoryIfEmpty(userDirectory);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        return deleted;
    }

    // handle find note by id
    @Override
    public NoteDto findNoteById(Long noteId) {
        // get logged in user id
        Long userId = userService.getCurrentUser().getId();
        Note note = noteRepository.findNoteByIdAndUser(noteId, userId);
        return note == null ? null : NoteMapper.mapToNoteDto(note);
    }

    // handle updating a note
    @Override
    public void updateNote(NoteDto note, Long noteId, Long clientId) {
        note.setId(noteId);
        Note updatedNote = NoteMapper.mapToNote(note);
        // get logged in user
        User user = userService.getCurrentUser();
        updatedNote.setClient(clientRepository.findClientByIdAndUser(clientId, user));
        noteRepository.save(updatedNote);
    }

    // handle deleting directory from filesystem if it is empty
    @Override
    public void deleteDirectoryIfEmpty(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            if (files.length == 0) {
                directory.delete();
            }
        }
    }

}
