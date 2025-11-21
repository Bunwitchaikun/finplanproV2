package com.finplanpro.finplanpro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    @NotEmpty
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
    
    private LocalDate dateOfBirth;
    
    private String gender;
}
