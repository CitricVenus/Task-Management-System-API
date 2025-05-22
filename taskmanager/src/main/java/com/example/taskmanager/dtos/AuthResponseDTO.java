package com.example.taskmanager.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;


public record AuthResponseDTO(String token) {
}
