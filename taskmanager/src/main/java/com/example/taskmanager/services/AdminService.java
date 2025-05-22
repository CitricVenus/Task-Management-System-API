package com.example.taskmanager.services;

import com.example.taskmanager.dtos.UserTaskInfoDTO;
import com.example.taskmanager.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserTaskInfoDTO> getUserTaskDetails() {
        return userRepository.findAll().stream()
                .map(user -> new UserTaskInfoDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getTasks().stream()
                                .map(task -> task.getTitle())
                                .toList()
                ))
                .toList();
    }
}