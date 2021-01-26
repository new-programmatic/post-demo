package ru.awg.rupost.demo.service;

import ru.awg.rupost.demo.exception.AuthenticationException;
import ru.awg.rupost.demo.exception.RecordNotFoundException;

public interface AuthService {

    String generateNewToken(String login, String password) throws AuthenticationException, RecordNotFoundException;

    Boolean checkToken(String token);
}
