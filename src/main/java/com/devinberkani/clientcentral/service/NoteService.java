package com.devinberkani.clientcentral.service;

import com.devinberkani.clientcentral.dto.ClientDto;
import com.devinberkani.clientcentral.dto.NoteDto;

public interface NoteService {

    void saveNewNote(NoteDto note, ClientDto client);
}
