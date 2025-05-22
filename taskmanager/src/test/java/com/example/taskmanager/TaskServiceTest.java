package com.example.taskmanager;


import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.services.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.taskmanager.repositories.TaskRepository;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    public TaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTasksByUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setUser(user);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setUser(user);

        when(taskRepository.findByUser(user)).thenReturn(Arrays.asList(task1, task2));

        // Act
        List<Task> tasks = taskService.getTasksByUser(user);

        // Assert
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());

        verify(taskRepository, times(1)).findByUser(user);
    }
}