package com.haiphamcoder.demo.adapter.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.haiphamcoder.demo.domain.entity.User;
import com.haiphamcoder.demo.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

}
