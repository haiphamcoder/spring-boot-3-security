package com.haiphamcoder.demo.application.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiphamcoder.demo.domain.entity.Token;
import com.haiphamcoder.demo.domain.entity.User;
import com.haiphamcoder.demo.domain.model.AuthenticationRequest;
import com.haiphamcoder.demo.domain.model.AuthenticationResponse;
import com.haiphamcoder.demo.domain.model.RegisterRequest;
import com.haiphamcoder.demo.domain.model.Role;
import com.haiphamcoder.demo.domain.model.TokenType;
import com.haiphamcoder.demo.infrastructure.security.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .role(request.getRole())
                .build();
        User createdUser = userService.createUser(user);
        revokeAllUserTokens(createdUser);
        String accessToken = jwtTokenProvider.generateToken(createdUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(createdUser);
        Token token = Token.builder()
                .user(createdUser)
                .tokenValue(accessToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        Token savedToken = tokenService.saveUserToken(token);
        return AuthenticationResponse.builder()
                .accessToken(savedToken.getTokenValue())
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        User user = userService.getUserByUsername(request.getUsername());
        if (user != null) {
            revokeAllUserTokens(user);
            String accessToken = jwtTokenProvider.generateToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);
            Token token = Token.builder()
                    .user(user)
                    .tokenValue(accessToken)
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .build();
            tokenService.saveUserToken(token);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        return null;
    }

    private void revokeAllUserTokens(User user) {
        List<Token> tokens = tokenService.getAllValidTokens(user.getId());
        tokens.forEach(token -> token.setRevoked(true));
        tokenService.saveAllUserTokens(tokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            String username = jwtTokenProvider.extractUsername(refreshToken);
            User user = userService.getUserByUsername(username);
            if (user != null) {
                revokeAllUserTokens(user);
                String accessToken = jwtTokenProvider.generateToken(user);
                String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
                Token token = Token.builder()
                        .user(user)
                        .tokenValue(accessToken)
                        .tokenType(TokenType.BEARER)
                        .expired(false)
                        .revoked(false)
                        .build();
                tokenService.saveUserToken(token);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public AuthenticationResponse createAdminUser() {
        RegisterRequest request = RegisterRequest.builder()
                .username("admin")
                .password("admin")
                .email("admin@email.com")
                .role(Role.ADMIN)
                .build();
        return register(request);
    }
}
