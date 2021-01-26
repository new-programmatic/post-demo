package ru.awg.rupost.demo.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.awg.rupost.AuthDataRequest;
import ru.awg.rupost.AuthDataResponse;
import ru.awg.rupost.AuthServiceGrpc.AuthServiceImplBase;
import ru.awg.rupost.demo.exception.AuthenticationException;
import ru.awg.rupost.demo.exception.RecordNotFoundException;
import ru.awg.rupost.demo.service.AuthService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class AuthServiceGrpc extends AuthServiceImplBase {

    private final AuthService authService;

    @Override
    public void auth(AuthDataRequest request, StreamObserver<AuthDataResponse> responseObserver) {
        try {
            String token = authService.generateNewToken(request.getLogin(), request.getPassword());
            AuthDataResponse response = AuthDataResponse.newBuilder()
                    .setToken(token)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (AuthenticationException | RecordNotFoundException e) {
            log.debug("Authentication error", e);
            responseObserver.onError(new StatusRuntimeException(Status.UNAUTHENTICATED.withDescription("Wrong login or password")));
        } catch (IllegalArgumentException e) {
            log.debug("Authentication error", e);
            responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(e.getMessage())));
        }
    }
}