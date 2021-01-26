package ru.awg.rupost.demo.grpc;

import io.grpc.internal.testing.StreamRecorder;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.awg.rupost.*;
import ru.awg.rupost.demo.AbstractSpringContextTest;
import ru.awg.rupost.demo.exception.RecordNotFoundException;
import ru.awg.rupost.demo.service.AuthService;
import ru.awg.rupost.demo.service.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceGrpcTest extends AbstractSpringContextTest {

    @MockBean
    AuthService authService;

    @MockBean
    ProductService productService;

    @Autowired
    ProductServiceGrpc productServiceGrpc;

    private ProductCard firstProductCard;
    private ProductCard secondProductCard;

    @Before
    @SneakyThrows
    public void setUp() {
        firstProductCard = ProductCard.newBuilder()
                .setTitle("Test title 1")
                .setRating(1.0)
                .setReviewsCount(2)
                .setPrice(999.99)
                .setImageUrl("/images/test-image-1.jpg")
                .setDescription("Test Description 1")
                .build();
        secondProductCard = ProductCard.newBuilder()
                .setTitle("Test title 2")
                .setRating(2.7)
                .setReviewsCount(100)
                .setPrice(743.63)
                .setImageUrl("/images/test-image-2.jpg")
                .setDescription("Test Description 2")
                .build();

        Mockito.when(productService.getProductById(1L)).thenReturn(firstProductCard);
        Mockito.when(productService.getProductById(3L)).thenThrow(new RecordNotFoundException("No product record exist for id: 3"));
        Mockito.when(productService.getProducts()).thenReturn(Arrays.asList(firstProductCard, secondProductCard));

        Mockito.when(authService.checkToken("test_token")).thenReturn(true);
        Mockito.when(authService.checkToken("test")).thenReturn(false);
    }

    @Test
    public void whenCallProductById_thenReturnProduct() {
        ProductCardRequest productCardRequest = ProductCardRequest.newBuilder()
                .setToken("test_token")
                .setId(1L)
                .build();

        StreamRecorder<ProductCardResponse> responseObserver = StreamRecorder.create();
        productServiceGrpc.product(productCardRequest, responseObserver);

        assertThat(responseObserver.getError()).isNull();
        List<ProductCardResponse> results = responseObserver.getValues();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getProducts()).isEqualTo(firstProductCard);
    }

    @Test
    public void whenCallProductById_invalidId_thenNotFoundMessage() {
        ProductCardRequest productCardRequest = ProductCardRequest.newBuilder()
                .setToken("test_token")
                .setId(3L)
                .build();

        StreamRecorder<ProductCardResponse> responseObserver = StreamRecorder.create();
        productServiceGrpc.product(productCardRequest, responseObserver);

        assertThat(responseObserver.getValues()).isEmpty();
        assertThat(responseObserver.getError()).isNotNull();
        assertThat(responseObserver.getError().getMessage()).isEqualTo("NOT_FOUND: No product record exist for id: 3");
    }

    @Test
    public void whenCallProductById_invalidToken_thenPermissionDeniedMessage() {
        ProductCardRequest productCardRequest = ProductCardRequest.newBuilder()
                .setToken("test")
                .setId(1L)
                .build();

        StreamRecorder<ProductCardResponse> responseObserver = StreamRecorder.create();
        productServiceGrpc.product(productCardRequest, responseObserver);

        assertThat(responseObserver.getValues()).isEmpty();
        assertThat(responseObserver.getError()).isNotNull();
        assertThat(responseObserver.getError().getMessage()).isEqualTo("PERMISSION_DENIED: Wrong token");
    }

    @Test
    public void whenCallProducts_thenReturnProducts() {
        ProductCardsRequest productCardsRequest = ProductCardsRequest.newBuilder()
                .setToken("test_token")
                .build();

        StreamRecorder<ProductCardsResponse> responseObserver = StreamRecorder.create();
        productServiceGrpc.products(productCardsRequest, responseObserver);

        assertThat(responseObserver.getError()).isNull();
        List<ProductCardsResponse> results = responseObserver.getValues();
        assertThat(results.get(0)).isEqualTo(ProductCardsResponse.newBuilder()
                .addAllProducts(Arrays.asList(firstProductCard, secondProductCard))
                .build());
    }

    @Test
    public void whenCallProducts_invalidToken_thenPermissionDeniedMessage() {
        ProductCardsRequest productCardsRequest = ProductCardsRequest.newBuilder()
                .setToken("test")
                .build();

        StreamRecorder<ProductCardsResponse> responseObserver = StreamRecorder.create();
        productServiceGrpc.products(productCardsRequest, responseObserver);

        assertThat(responseObserver.getValues()).isEmpty();
        assertThat(responseObserver.getError()).isNotNull();
        assertThat(responseObserver.getError().getMessage()).isEqualTo("PERMISSION_DENIED: Wrong token");
    }
}
