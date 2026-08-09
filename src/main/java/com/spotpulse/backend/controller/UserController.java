package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.User;
import com.spotpulse.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/test")
    public User createTestUser() {
        User user = new User("test-uid-001", "테스트유저");
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}