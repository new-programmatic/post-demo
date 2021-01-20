package ru.awg.rupost.demo.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.awg.rupost.AuthDataRequest;
import ru.awg.rupost.AuthDataResponse;
import ru.awg.rupost.AuthServiceGrpc.AuthServiceImplBase;
import ru.awg.rupost.demo.service.AuthService;

@GrpcService
@RequiredArgsConstructor
public class AuthServiceGrpc extends AuthServiceImplBase {

    private final AuthService authService;

    @Override
    public void auth(AuthDataRequest request, StreamObserver<AuthDataResponse> responseObserver) {
        String token = authService.generateNewToken(request.getLogin(), request.getPassword());
        AuthDataResponse response = AuthDataResponse.newBuilder()
                .setToken(token)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}