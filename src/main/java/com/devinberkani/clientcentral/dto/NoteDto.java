package com.devinberkani.clientcentral.dto;

import com.devinberkani.clientcentral.entity.Client;
import com.devinberkani.clientcentral.entity.FileAttachment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDto {

    private Long id;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    @NotEmpty(message = "Note content should not be empty")
    @Size(max = 255, message = "Content should be no more than 255 characters")
    private String content;
    private Client client;
    private List<FileAttachment> files = new ArrayList<>();

}
