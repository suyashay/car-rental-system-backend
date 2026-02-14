package com.carrental.backend.service;


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

    public String register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return "Email already exists";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.CUSTOMER);

        userRepository.save(user);

        return "User registered successfully";
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return "Login successful";
        }

        return "Invalid credentials";
    }

}
