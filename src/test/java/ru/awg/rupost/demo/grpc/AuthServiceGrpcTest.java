package ru.awg.rupost.demo.grpc;

import io.grpc.internal.testing.StreamRecorder;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.awg.rupost.AuthDataRequest;
import ru.awg.rupost.AuthDataResponse;
import ru.awg.rupost.demo.AbstractSpringContextTest;
import ru.awg.rupost.demo.exception.AuthenticationException;
import ru.awg.rupost.demo.service.AuthService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthServiceGrpcTest extends AbstractSpringContextTest {

    @MockBean
    AuthService authService;

    @Autowired
    AuthServiceGrpc authServiceGrpc;

    @Before
    @SneakyThrows
    public void setUp() {
        Mockito.when(authService.generateNewToken("login", "password")).thenReturn("test_token");
        Mockito.when(authService.generateNewToken("test1", "password1")).thenThrow(new AuthenticationException("No users found for login: test1"));
        Mockito.when(authService.generateNewToken("test2", "password2")).thenThrow(new AuthenticationException("Wrong password for login: test2"));
        Mockito.when(authService.generateNewToken("", "pass")).thenThrow(new IllegalArgumentException("Invalid login"));
        Mockito.when(authService.generateNewToken("test3", "")).thenThrow(new IllegalArgumentException("Invalid password"));
    }

    @Test
    public void whenCallAuth_thenReturnsToken() {
        AuthDataRequest authDataRequest = AuthDataRequest.newBuilder()
                .setLogin("login")
                .setPassword("password")
                .build();

        StreamRecorder<AuthDataResponse> responseObserver = StreamRecorder.create();
        authServiceGrpc.auth(authDataRequest, responseObserver);

        assertThat(responseObserver.getError()).isNull();
        List<AuthDataResponse> results = responseObserver.getValues();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getToken()).isEqualTo("test_token");
    }

    @Test
    public void whenCallAuth_wrongLogin_thenReturnsUnauthenticatedMessage() {
        AuthDataRequest authDataRequest = AuthDataRequest.newBuilder()
                .setLogin("test1")
                .setPassword("password1")
                .build();

        StreamRecorder<AuthDataResponse> responseObserver = StreamRecorder.create();
        authServiceGrpc.auth(authDataRequest, responseObserver);

        assertThat(responseObserver.getValues()).isEmpty();
        assertThat(responseObserver.getError()).isNotNull();
        assertThat(responseObserver.getError().getMessage()).isEqualTo("UNAUTHENTICATED: Wrong login or password");
    }

    @Test
    public void whenCallAuth_wrongPassword_thenReturnsUnauthenticatedMessage() {
        AuthDataRequest authDataRequest = AuthDataRequest.newBuilder()
                .setLogin("test2")
                .setPassword("password2")
                .build();

        StreamRecorder<AuthDataResponse> responseObserver = StreamRecorder.create();
        authServiceGrpc.auth(authDataRequest, responseObserver);

        assertThat(responseObserver.getValues()).isEmpty();
        assertThat(responseObserver.getError()).isNotNull();
        assertThat(responseObserver.getError().getMessage()).isEqualTo("UNAUTHENTICATED: Wrong login or password");
    }

    @Test
    public void whenCallAuth_invalidLogin_thenReturnsInvalidArgumentMessage() {
        AuthDataRequest authDataRequest = AuthDataRequest.newBuilder()
                .setLogin("")
                .setPassword("pass")
                .build();

        StreamRecorder<AuthDataResponse> responseObserver = StreamRecorder.create();
        authServiceGrpc.auth(authDataRequest, responseObserver);

        assertThat(responseObserver.getValues()).isEmpty();
        assertThat(responseObserver.getError()).isNotNull();
        assertThat(responseObserver.getError().getMessage()).isEqualTo("INVALID_ARGUMENT: Invalid login");
    }

    @Test
    public void whenCallAuth_invalidPassword_thenReturnsInvalidArgumentMessage() {
        AuthDataRequest authDataRequest = AuthDataRequest.newBuilder()
                .setLogin("test3")
                .setPassword("")
                .build();

        StreamRecorder<AuthDataResponse> responseObserver = StreamRecorder.create();
        authServiceGrpc.auth(authDataRequest, responseObserver);

        assertThat(responseObserver.getValues()).isEmpty();
        assertThat(responseObserver.getError()).isNotNull();
        assertThat(responseObserver.getError().getMessage()).isEqualTo("INVALID_ARGUMENT: Invalid password");
    }
}
