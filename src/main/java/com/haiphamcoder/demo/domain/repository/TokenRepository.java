package com.haiphamcoder.demo.domain.repository;

import java.util.List;
import java.util.Optional;

import com.haiphamcoder.demo.domain.entity.Token;

public interface TokenRepository {
    List<Token> getAllValidTokensByUserId(Long userId);

    Optional<Token> getTokenByTokenValue(String tokenValue);

    Optional<Token> getValidTokenByUserIdAndTokenValue(Long userId, String tokenValue);

    Token saveToken(Token token);

    void saveAllTokens(List<Token> tokens);
}
