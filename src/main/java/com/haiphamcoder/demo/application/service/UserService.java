package com.haiphamcoder.demo.application.service;

import java.util.Optional;

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

    public void getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        Optional<User> user = userRepository.getUserByUsername(username);
        if (user.isEmpty()) {
            log.error("User with username {} not found", username);
            return;
        }
        log.info("User found: {}", user.get());
    }
}
