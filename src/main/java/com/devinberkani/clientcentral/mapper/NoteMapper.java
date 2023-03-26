package com.devinberkani.clientcentral.mapper;

import com.devinberkani.clientcentral.dto.NoteDto;
import com.devinberkani.clientcentral.entity.Note;

public class NoteMapper {

    public static NoteDto mapToNoteDto(Note note) {
        return NoteDto.builder()
                .id(note.getId())
                .createdOn(note.getCreatedOn())
                .updatedOn(note.getUpdatedOn())
                .content(note.getContent())
                .client(note.getClient())
                .files(note.getFiles())
                .build();
    }

    public static Note mapToNote(NoteDto noteDto) {
        return Note.builder()
                .id(noteDto.getId())
                .createdOn(noteDto.getCreatedOn())
                .updatedOn(noteDto.getUpdatedOn())
                .content(noteDto.getContent())
                .build();
    }

}
