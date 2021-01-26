package ru.awg.rupost.demo.service;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.awg.rupost.ProductCard;
import ru.awg.rupost.demo.AbstractSpringContextTest;
import ru.awg.rupost.demo.dao.ProductsDao;
import ru.awg.rupost.demo.exception.RecordNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ProductServiceTest extends AbstractSpringContextTest {

    @MockBean
    ProductsDao productsDao;

    @Autowired
    ProductService productService;

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

        Mockito.when(productsDao.getProductById(1L)).thenReturn(firstProductCard);
        Mockito.when(productsDao.getProductById(3L)).thenThrow(new RecordNotFoundException("No product record exist for id: 3"));
        Mockito.when(productsDao.getProducts()).thenReturn(Arrays.asList(firstProductCard, secondProductCard));
    }

    @Test
    @SneakyThrows
    public void whenValidId_thenProductShouldBeFound() {
        ProductCard foundProduct = productService.getProductById(1L);

        assertThat(firstProductCard).isEqualTo(foundProduct);
    }

    @Test
    public void whenInvalidId_thenNoProductFoundException() {
        assertThatThrownBy(() -> productService.getProductById(3L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessage("No product record exist for id: 3");
    }

    @Test
    public void whenGetProducts_thenProductsShouldBeFound() {
        List<ProductCard> products = productService.getProducts();

        assertThat(products).hasSize(2);
        assertThat(Arrays.asList(firstProductCard, secondProductCard)).isEqualTo(products);
    }
}
