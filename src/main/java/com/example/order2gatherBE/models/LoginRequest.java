package com.example.order2gatherBE.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public class LoginRequest {
    @NotEmpty(message = "The email is required.")
    @Email(message = "The email is not a valid email.")
    private String email;

    @NotBlank(message = "The username is required.")
    private String username;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
