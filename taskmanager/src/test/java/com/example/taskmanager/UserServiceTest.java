package com.example.taskmanager;


import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder; // si usas para cifrar la pass

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUsername_UserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(Role.USER);
        user.setPassword("hashedpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = Optional.ofNullable(userService.findByUsername("testuser"));

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());

        Optional<User> result = Optional.ofNullable(userService.findByUsername("notfound"));

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername("notfound");
    }

    @Test
    public void testSaveUser_ShouldEncodePasswordAndSave() {
        User rawUser = new User();
        rawUser.setUsername("newuser");
        rawUser.setPassword("plainpassword");
        rawUser.setRole(Role.USER);

        when(passwordEncoder.encode("plainpassword")).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.saveUser(rawUser);

        assertEquals("encodedpassword", savedUser.getPassword());
        assertEquals("newuser", savedUser.getUsername());
        assertEquals(Role.USER, savedUser.getRole());

        verify(passwordEncoder, times(1)).encode("plainpassword");
        verify(userRepository, times(1)).save(any(User.class));
    }
}