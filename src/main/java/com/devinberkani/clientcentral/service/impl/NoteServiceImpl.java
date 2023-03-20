package com.devinberkani.clientcentral.service.impl;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.entity.Note;
import com.devinberkani.clientcentral.mapper.ClientMapper;
import com.devinberkani.clientcentral.mapper.NoteMapper;
import com.devinberkani.clientcentral.repository.NoteRepository;
import com.devinberkani.clientcentral.service.NoteService;

public class NoteServiceImpl implements NoteService {

    NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void saveNewNote(NoteDto note, ClientDto client) {
        Note newNote = NoteMapper.mapToNote(note);
        // set client owner for note
        note.setClient(ClientMapper.mapToClient(client));
        // save note to repository
        noteRepository.save(newNote);
    }
}
