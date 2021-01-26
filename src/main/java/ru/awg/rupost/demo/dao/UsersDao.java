package ru.awg.rupost.demo.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.awg.rupost.demo.dao.model.UserEntity;
import ru.awg.rupost.demo.exception.RecordNotFoundException;
import ru.awg.rupost.demo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UsersDao {

    private final UserRepository userRepository;

    public UserEntity getUserByLogin(String login) throws RecordNotFoundException {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RecordNotFoundException(String.format("No users found for login: %s", login)));
    }

    public boolean existsByToken(String token) {
        return userRepository.existsByToken(token);
    }

    public void updateUserToken(Long id, String token) {
        userRepository.updateUserTokenById(id, token);
    }
}
