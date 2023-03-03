package com.devinberkani.clientcentral.dto;

import com.devinberkani.clientcentral.entity.Note;
import com.devinberkani.clientcentral.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private Long id;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    @NotEmpty(message = "First name should not be empty")
    private String firstName;
    @NotEmpty(message = "Last name should not be empty")
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;
    @NotEmpty(message = "Birthday should not be empty")
    private LocalDate birthday;
    private User user;
    private List<Note> notes = new ArrayList<>();
}
