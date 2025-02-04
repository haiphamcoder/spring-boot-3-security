package com.haiphamcoder.demo.domain.repository;

import java.util.Optional;

import com.haiphamcoder.demo.domain.entity.User;

public interface UserRepository {
    Optional<User> getUserByUsername(String username);
}
