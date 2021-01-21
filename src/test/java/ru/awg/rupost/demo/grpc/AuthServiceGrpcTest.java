package ru.awg.rupost.demo.grpc;

import io.grpc.internal.testing.StreamRecorder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.awg.rupost.AuthDataRequest;
import ru.awg.rupost.AuthDataResponse;
import ru.awg.rupost.demo.AbstractSpringContextTest;
import ru.awg.rupost.demo.service.AuthService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthServiceGrpcTest extends AbstractSpringContextTest {

    @MockBean
    AuthService authService;

    @Autowired
    AuthServiceGrpc authServiceGrpc;

    @Before
    public void setUp() {
        Mockito.when(authService.generateNewToken("login", "password")).thenReturn("test_token");
    }

    @Test
    public void whenCallAuth_thenReturnsValidToken() {
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
}
