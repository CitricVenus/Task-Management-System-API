package com.example.taskmanager.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record UserLoginDTO(@NotBlank String username, @NotBlank String password)  {
}
