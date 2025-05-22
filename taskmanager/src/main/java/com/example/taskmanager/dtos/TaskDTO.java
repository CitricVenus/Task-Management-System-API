package com.example.taskmanager.dtos;

import com.example.taskmanager.enums.Priority;
import com.example.taskmanager.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record TaskDTO(Long id, @NotBlank String title, @NotBlank String description, @NotNull Status status, @NotNull Priority priority, LocalDate dueDate) {
}