package com.example.taskmanager.controllers;

import com.example.taskmanager.dtos.TaskDTO;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.services.TaskService;
import com.example.taskmanager.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService; // Para obtener usuario por username

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // Listar tareas propias o todas si es admin
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);

        List<Task> tasks;
        if (currentUser.getRole() == Role.ADMIN) {
            tasks = taskService.getAllTasks();
        } else {
            tasks = taskService.getTasksByUserId(currentUser.getId());
        }

        List<TaskDTO> dtos = tasks.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Crear tarea
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);

        Task task = toEntity(taskDTO);
        task.setUser(currentUser);
        Task saved = taskService.createTask(task);

        return ResponseEntity.ok(toDTO(saved));
    }

    // Actualizar tarea (solo dueño o admin)
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);

        Task existingTask = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!existingTask.getUser().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        existingTask.setTitle(taskDTO.title());
        existingTask.setDescription(taskDTO.description());
        existingTask.setStatus(taskDTO.status());
        existingTask.setPriority(taskDTO.priority());
        existingTask.setDueDate(taskDTO.dueDate());

        Task updated = taskService.updateTask(existingTask);
        return ResponseEntity.ok(toDTO(updated));
    }

    // Eliminar tarea (solo dueño o admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);

        Task existingTask = taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!existingTask.getUser().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/mytasks")
    public ResponseEntity<List<TaskDTO>> getUserTasks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);

        List<Task> tasks = taskService.getTasksByUser(currentUser);
        List<TaskDTO> taskDTOs = tasks.stream().map(this::toDTO).toList();

        return ResponseEntity.ok(taskDTOs);
    }


    // Mapeos entre DTO y entidad (puedes usar MapStruct o hacerlo manual)
    private TaskDTO toDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate()
        );
    }

    private Task toEntity(TaskDTO dto) {
        return Task.builder()
                .title(dto.title())
                .description(dto.description())
                .status(dto.status())
                .priority(dto.priority())
                .dueDate(dto.dueDate())
                .build();
    }
}