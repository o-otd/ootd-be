package com.ootd.be.config.security.jwt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TokenRepository {

    private final Map<String, String> tokenRepository = new HashMap<>();

    public boolean validate(String username, String refreshToken) {
        return tokenRepository.getOrDefault(username, "").equals(refreshToken);
    }

    public void update(String username, String refreshToken) {
        tokenRepository.put(username, refreshToken);
    }

    public void remove(String username) {
        tokenRepository.remove(username);
    }

}
