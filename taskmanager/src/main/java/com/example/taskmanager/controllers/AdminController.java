package com.example.taskmanager.controllers;

import com.example.taskmanager.dtos.UserTaskInfoDTO;
import com.example.taskmanager.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<UserTaskInfoDTO>> getDashboard() {
        return ResponseEntity.ok(adminService.getUserTaskDetails());
    }
}