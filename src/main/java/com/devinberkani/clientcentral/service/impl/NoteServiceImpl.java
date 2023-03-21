package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.entity.Note;
import com.devinberkani.clientcentral.mapper.NoteMapper;
import com.devinberkani.clientcentral.repository.ClientRepository;
import com.devinberkani.clientcentral.repository.NoteRepository;
import com.devinberkani.clientcentral.service.NoteService;
import org.springframework.stereotype.Service;

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
}
