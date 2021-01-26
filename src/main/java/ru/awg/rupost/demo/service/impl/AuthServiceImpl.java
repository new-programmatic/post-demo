package ru.awg.rupost.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.awg.rupost.demo.dao.UsersDao;
import ru.awg.rupost.demo.dao.model.UserEntity;
import ru.awg.rupost.demo.exception.AuthenticationException;
import ru.awg.rupost.demo.exception.RecordNotFoundException;
import ru.awg.rupost.demo.service.AuthService;

import java.security.SecureRandom;
import java.util.Base64;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersDao usersDao;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Override
    public String generateNewToken(String login, String password) throws AuthenticationException, RecordNotFoundException {
        checkArgument(login != null && !login.isEmpty(), "Invalid login");
        checkArgument(password != null && !password.isEmpty(), "Invalid password");

        UserEntity user = usersDao.getUserByLogin(login);
        String key = login + ":" + password;
        String encodedPassword = base64Encoder.encodeToString(key.getBytes());

        if (encodedPassword.equals(user.getPassword())) {
            byte[] randomBytes = new byte[24];
            secureRandom.nextBytes(randomBytes);
            String token = base64Encoder.encodeToString(randomBytes);

            usersDao.updateUserToken(user.getId(), token);

            return token;
        } else {
            throw new AuthenticationException(String.format("Wrong password for login: %s", login));
        }
    }

    @Override
    public Boolean checkToken(String token) {
        return usersDao.existsByToken(token);
    }
}
