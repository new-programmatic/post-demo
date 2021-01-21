package ru.awg.rupost.demo.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.awg.rupost.demo.dao.model.MediaEntity;
import ru.awg.rupost.demo.dao.model.ProductEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindById_thenReturnProduct() {
        MediaEntity image = MediaEntity.builder()
                .path("/images/test-image.jpg")
                .build();
        ProductEntity preparedProduct = ProductEntity.builder()
                .title("Test title")
                .rating(1.0)
                .reviewsCount(2)
                .price(new BigDecimal(99999))
                .media(image)
                .description("Test Description")
                .build();

        entityManager.persist(image);
        entityManager.persist(preparedProduct);
        entityManager.flush();

        Optional<ProductEntity> foundProduct = productRepository.findById(preparedProduct.getId());

        assertThat(foundProduct).isNotEmpty();
        assertThat(preparedProduct).isEqualTo(foundProduct.get());
    }

    @Test
    public void whenFindAll_thenReturnProducts() {
        MediaEntity firstImage = MediaEntity.builder()
                .path("/images/test-image-1.jpg")
                .build();
        MediaEntity secondImage = MediaEntity.builder()
                .path("/images/test-image-2.jpg")
                .build();
        ProductEntity firstPreparedProduct = ProductEntity.builder()
                .title("Test title 1")
                .rating(1.0)
                .reviewsCount(2)
                .price(new BigDecimal(99999))
                .media(firstImage)
                .description("Test Description 1")
                .build();
        ProductEntity secondPreparedProduct = ProductEntity.builder()
                .title("Test title 2")
                .rating(2.7)
                .reviewsCount(100)
                .price(new BigDecimal(74363))
                .media(secondImage)
                .description("Test Description 2")
                .build();

        entityManager.persist(firstImage);
        entityManager.persist(secondImage);
        entityManager.persist(firstPreparedProduct);
        entityManager.persist(secondPreparedProduct);
        entityManager.flush();

        List<ProductEntity> products = productRepository.findAll();

        assertThat(products).hasSize(2);
        assertThat(Arrays.asList(firstPreparedProduct, secondPreparedProduct)).isEqualTo(products);
    }
}
