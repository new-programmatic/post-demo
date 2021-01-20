package ru.awg.rupost.demo.service.impl;

import org.springframework.stereotype.Service;
import ru.awg.rupost.demo.service.AuthService;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
    private final Map<String, String> tokenHolder = new ConcurrentHashMap<>();

    @Override
    public String generateNewToken(String clientId, String clientSecret) {
        String key = clientId + ":" + clientSecret;

        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);

        tokenHolder.put(key, token);
        return token;
    }

    @Override
    public Boolean checkToken(String token) {
        return tokenHolder.containsValue(token);
    }
}
