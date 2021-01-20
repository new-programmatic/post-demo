package ru.awg.rupost.demo.service;

public interface AuthService {

    String generateNewToken(String clientId, String clientSecret);

    Boolean checkToken(String token);
}
