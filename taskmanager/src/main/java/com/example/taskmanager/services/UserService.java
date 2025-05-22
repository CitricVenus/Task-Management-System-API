package com.example.taskmanager.services;


import com.example.taskmanager.ResourceNotFoundException;
import com.example.taskmanager.dtos.UserRegisterDTO;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserRegisterDTO registerDTO){
        if (userRepository.existsByUsername(registerDTO.username())){
            throw new RuntimeException("Username: '" +registerDTO.username() + "' already exist");
        }
        User user = User.builder()
                .username(registerDTO.username())
                .password(registerDTO.password())
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("Username not found: " + username));
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }



}
