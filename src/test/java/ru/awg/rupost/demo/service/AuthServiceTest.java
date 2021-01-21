package ru.awg.rupost.demo.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.awg.rupost.demo.AbstractSpringContextTest;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthServiceTest extends AbstractSpringContextTest {

    @Autowired
    AuthService authService;

    @Test
    public void whenGeneratingNewToken_thenTokenIsValid() {
        String token = authService.generateNewToken("test", "password");

        assertThat(token).isNotEmpty();
        assertThat(authService.checkToken(token)).isTrue();
    }
}
