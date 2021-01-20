package ru.awg.rupost.demo.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.awg.rupost.*;
import ru.awg.rupost.ProductServiceGrpc.ProductServiceImplBase;
import ru.awg.rupost.demo.exception.RecordNotFoundException;
import ru.awg.rupost.demo.service.AuthService;
import ru.awg.rupost.demo.service.ProductService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ProductServiceGrpc extends ProductServiceImplBase {

    private final AuthService authService;
    private final ProductService productService;

    @Override
    public void product(ProductCardRequest request, StreamObserver<ProductCardResponse> responseObserver) {
        String token = request.getToken();
        if (authService.checkToken(token)) {
            try {
                ProductCard product = productService.getProductById(request.getId());
                ProductCardResponse response = ProductCardResponse.newBuilder()
                        .setProducts(product)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (RecordNotFoundException e) {
                responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription(e.getMessage())));
            } catch (Throwable e) {
                log.error("Error while getting product by id", e);
                responseObserver.onError(new StatusRuntimeException(Status.INTERNAL.withDescription("Server error while getting product by id")));
            }
        } else {
            responseObserver.onError(new StatusRuntimeException(Status.UNAUTHENTICATED.withDescription("Wrong token")));
        }
    }

    @Override
    public void products(ProductCardsRequest request, StreamObserver<ProductCardsResponse> responseObserver) {
        String token = request.getToken();
        if (authService.checkToken(token)) {
            try {
                List<ProductCard> productsList = productService.getProducts();
                ProductCardsResponse response = ProductCardsResponse.newBuilder()
                        .addAllProducts(productsList)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (Throwable e) {
                log.error("Error while getting products", e);
                responseObserver.onError(new StatusRuntimeException(Status.INTERNAL.withDescription("Server error while getting products")));
            }
        } else {
            responseObserver.onError(new StatusRuntimeException(Status.UNAUTHENTICATED.withDescription("Wrong token")));
        }
    }
}
