package ru.awg.rupost.demo;

import ru.awg.rupost.demo.service.AuthServiceImpl;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.awg.rupost.AuthDataResponse;

@GrpcService
public class AuthServiceGrpc extends ru.awg.rupost.AuthServiceGrpc.AuthServiceImplBase {

    @Autowired
    AuthServiceImpl authService;

    @Override
    public void auth(ru.awg.rupost.AuthDataRequest request,
                     io.grpc.stub.StreamObserver<ru.awg.rupost.AuthDataResponse> responseObserver) {
        String token = authService.generateNewToken(request.getLogin(), request.getPassword());
        AuthDataResponse response = AuthDataResponse.newBuilder()
                .setToken(token)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}