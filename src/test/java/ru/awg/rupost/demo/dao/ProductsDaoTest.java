package ru.awg.rupost.demo.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.awg.rupost.ProductCard;
import ru.awg.rupost.demo.AbstractSpringContextTest;
import ru.awg.rupost.demo.dao.model.MediaEntity;
import ru.awg.rupost.demo.dao.model.ProductEntity;
import ru.awg.rupost.demo.exception.RecordNotFoundException;
import ru.awg.rupost.demo.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ProductsDaoTest extends AbstractSpringContextTest {

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductsDao productsDao;

    @Before
    public void setUp() {
        MediaEntity firstImage = MediaEntity.builder()
                .id(1L)
                .path("/images/test-image-1.jpg")
                .build();
        MediaEntity secondImage = MediaEntity.builder()
                .id(2L)
                .path("/images/test-image-2.jpg")
                .build();
        ProductEntity firstPreparedProduct = ProductEntity.builder()
                .id(1L)
                .title("Test title 1")
                .rating(1.0)
                .reviewsCount(2)
                .price(new BigDecimal(99999))
                .media(firstImage)
                .description("Test Description 1")
                .build();
        ProductEntity secondPreparedProduct = ProductEntity.builder()
                .id(2L)
                .title("Test title 2")
                .rating(2.7)
                .reviewsCount(100)
                .price(new BigDecimal(74363))
                .media(secondImage)
                .description("Test Description 2")
                .build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(firstPreparedProduct));
        Mockito.when(productRepository.findById(3L)).thenReturn(Optional.empty());
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(firstPreparedProduct, secondPreparedProduct));
    }

    @Test
    public void whenValidId_thenProductShouldBeFound() throws RecordNotFoundException {
        ProductCard productCard = ProductCard.newBuilder()
                .setTitle("Test title 1")
                .setRating(1.0)
                .setReviewsCount(2)
                .setPrice(999.99)
                .setImageUrl("/images/test-image-1.jpg")
                .setDescription("Test Description 1")
                .build();

        ProductCard foundProduct = productsDao.getProductById(1L);

        assertThat(productCard).isEqualTo(foundProduct);
    }

    @Test
    public void whenInvalidId_thenNoProductFoundException() {
        assertThatThrownBy(() -> productsDao.getProductById(3L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessage("No product record exist for id: 3");
    }

    @Test
    public void whenGetProducts_thenProductsShouldBeFound() {
        ProductCard firstProductCard = ProductCard.newBuilder()
                .setTitle("Test title 1")
                .setRating(1.0)
                .setReviewsCount(2)
                .setPrice(999.99)
                .setImageUrl("/images/test-image-1.jpg")
                .setDescription("Test Description 1")
                .build();
        ProductCard secondProductCard = ProductCard.newBuilder()
                .setTitle("Test title 2")
                .setRating(2.7)
                .setReviewsCount(100)
                .setPrice(743.63)
                .setImageUrl("/images/test-image-2.jpg")
                .setDescription("Test Description 2")
                .build();

        List<ProductCard> products = productsDao.getProducts();

        assertThat(products).hasSize(2);
        assertThat(Arrays.asList(firstProductCard, secondProductCard)).isEqualTo(products);
    }
}
