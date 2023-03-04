package com.devinberkani.clientcentral.dto;

import com.devinberkani.clientcentral.entity.Note;
import com.devinberkani.clientcentral.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

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

    // phone number either needs to be empty or match the specified pattern 555-555-5555
    @Pattern(regexp = "^$|\\d{3}-\\d{3}-\\d{4}", message = "Invalid format. Phone number must be empty or in the format 555-555-5555.")
    private String phoneNumber;
    private String email;
    private LocalDate birthday;
    private User user;
    private List<Note> notes = new ArrayList<>();
}
