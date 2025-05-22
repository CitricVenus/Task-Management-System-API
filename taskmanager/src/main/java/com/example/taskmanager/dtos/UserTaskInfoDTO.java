package com.example.taskmanager.dtos;

import java.util.List;

public record UserTaskInfoDTO(
        Long userId,
        String username,
        List<String> taskTitles
) {}