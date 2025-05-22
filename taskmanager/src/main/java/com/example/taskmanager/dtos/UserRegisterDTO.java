package com.example.taskmanager.dtos;


import jakarta.validation.constraints.NotBlank;

public record UserRegisterDTO(
        @NotBlank String username,
        @NotBlank String password,
        String role
) {}