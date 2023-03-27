package com.devinberkani.clientcentral.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name should only contain letters")
    @NotEmpty(message = "First name should not be empty")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name should only contain letters")
    @NotEmpty(message = "Last name should not be empty")
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;
}
