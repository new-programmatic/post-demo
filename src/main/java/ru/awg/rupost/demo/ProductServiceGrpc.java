package ru.awg.rupost.demo;

import org.springframework.beans.factory.annotation.Autowired;
import ru.awg.rupost.AuthDataResponse;
import ru.awg.rupost.ProductCardsResponse;
import ru.awg.rupost.demo.service.AuthServiceImpl;
import ru.awg.rupost.demo.service.ProductService;

public class ProductServiceGrpc extends ru.awg.rupost.ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    ProductService productService;

    @Override
    public void products(ru.awg.rupost.ProductCardsRequest request,
                         io.grpc.stub.StreamObserver<ru.awg.rupost.ProductCardsResponse> responseObserver) {
        String token = request.getToken();
        if(authService.checkToken(token)){
            // Тут данные
            ProductCardsResponse response = ProductCardsResponse.newBuilder()
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }else {
            // Тут эксепшен
        }
    }


}
