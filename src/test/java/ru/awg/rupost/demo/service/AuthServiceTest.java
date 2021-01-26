package ru.awg.rupost.demo.service;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.awg.rupost.demo.AbstractSpringContextTest;
import ru.awg.rupost.demo.dao.UsersDao;
import ru.awg.rupost.demo.dao.model.UserEntity;
import ru.awg.rupost.demo.exception.AuthenticationException;
import ru.awg.rupost.demo.exception.RecordNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

public class AuthServiceTest extends AbstractSpringContextTest {

    @MockBean
    UsersDao usersDao;

    @Autowired
    AuthService authService;

    @Before
    @SneakyThrows
    public void setUp() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .login("test1")
                .password("dGVzdDE6cGFzc3dvcmQx")
                .build();
        Mockito.when(usersDao.getUserByLogin("test1")).thenReturn(user);
        Mockito.when(usersDao.getUserByLogin("test2")).thenThrow(new RecordNotFoundException("No users found for login: test2"));
        Mockito.when(usersDao.existsByToken("test_token")).thenReturn(true);
        Mockito.when(usersDao.existsByToken("test")).thenReturn(false);
    }

    @Test
    @SneakyThrows
    public void whenAuth_thenCallUpdateUserTokenMethod() {
        authService.generateNewToken("test1", "password1");

        Mockito.verify(usersDao, Mockito.times(1)).updateUserToken(eq(1L), anyString());
    }

    @Test
    @SneakyThrows
    public void whenAuth_wrongLogin_thenNoUsersFoundMessage() {
        assertThatThrownBy(() -> authService.generateNewToken("test2", "password1"))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessage("No users found for login: test2");
    }

    @Test
    @SneakyThrows
    public void whenAuth_wrongPassword_thenWrongPasswordMessage() {
        assertThatThrownBy(() -> authService.generateNewToken("test1", "password2"))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Wrong password for login: test1");
    }

    @Test
    @SneakyThrows
    public void whenAuth_invalidLogin_thenInvalidLoginMessage() {
        assertThatThrownBy(() -> authService.generateNewToken("", "password1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid login");
    }

    @Test
    @SneakyThrows
    public void whenAuth_invalidPassword_thenInvalidPasswordMessage() {
        assertThatThrownBy(() -> authService.generateNewToken("test1", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password");
    }

    @Test
    @SneakyThrows
    public void whenCheckToken_validToken_thenReturnTrue() {
        assertThat(authService.checkToken("test_token"))
                .isTrue();
    }

    @Test
    @SneakyThrows
    public void whenCheckToken_wrongToken_thenReturnFalse() {
        assertThat(authService.checkToken("test"))
                .isFalse();
    }
}
