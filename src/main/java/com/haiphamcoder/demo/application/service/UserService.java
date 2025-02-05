package com.haiphamcoder.demo.application.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.haiphamcoder.demo.domain.entity.User;
import com.haiphamcoder.demo.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        Optional<User> user = userRepository.getUserByUsername(username);
        if (user.isEmpty()) {
            log.error("User with username {} not found", username);
            return null;
        }
        log.info("User found: {}", user.get());
        return user.get();
    }

    public User createUser(User user) {
        log.info("Creating user: {}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveUser(user);
    }
}
