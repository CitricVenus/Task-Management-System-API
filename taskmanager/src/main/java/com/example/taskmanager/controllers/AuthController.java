package com.example.taskmanager.controllers;


import com.example.taskmanager.dtos.AuthResponseDTO;
import com.example.taskmanager.dtos.UserLoginDTO;
import com.example.taskmanager.dtos.UserRegisterDTO;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.jwt.JwtUtil;
import com.example.taskmanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO request){
        String rawPassword = request.password();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        try{

            User user = new User();
            user.setUsername(request.username());
            user.setPassword(encodedPassword);
            user.setRole(Role.valueOf(request.role().toUpperCase()));
            userService.saveUser(user);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Invalid Role");
        }
        return ResponseEntity.ok("User registered");
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid UserLoginDTO request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(),request.password())
        );
        User user  = userService.findByUsername(request.username());
        String token = jwtUtil.generateToken(user.getUsername(),user.getRole().name());
        return ResponseEntity.ok(new AuthResponseDTO(token));

    }
}
