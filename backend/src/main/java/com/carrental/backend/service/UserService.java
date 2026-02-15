package com.carrental.backend.service;


import com.carrental.backend.dto.AuthResponse;
import com.carrental.backend.dto.LoginRequest;
import com.carrental.backend.dto.RegisterRequest;
import com.carrental.backend.entity.User;
import com.carrental.backend.entity.enums.UserRole;
import com.carrental.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");

        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.CUSTOMER);

        userRepository.save(user);

        return new AuthResponse("User registered Successfully");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return new AuthResponse("Login successful");
        }

        throw new RuntimeException("Invalid credentials");
    }

}
