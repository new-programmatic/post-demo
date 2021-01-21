package ru.awg.rupost.demo.converter;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.awg.rupost.ProductCard;
import ru.awg.rupost.demo.AbstractSpringContextTest;
import ru.awg.rupost.demo.dao.model.MediaEntity;
import ru.awg.rupost.demo.dao.model.ProductEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperTest extends AbstractSpringContextTest {

    @Autowired
    ProductMapper productMapper;

    @Test
    public void checkConvertProductEntityToProductDto() {
        MediaEntity mediaEntity = MediaEntity.builder()
                .id(1L)
                .path("/images/some-image-1.jpg")
                .build();
        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .title("Test title")
                .rating(1.0)
                .reviewsCount(2)
                .price(new BigDecimal(99999))
                .media(mediaEntity)
                .description("Test Description")
                .build();

        ProductCard productCard = productMapper.toDto(productEntity);

        assertThat(productCard.getTitle()).isEqualTo(productEntity.getTitle());
        assertThat(productCard.getRating()).isEqualTo(productEntity.getRating());
        assertThat(productCard.getReviewsCount()).isEqualTo(productEntity.getReviewsCount());
        assertThat(productCard.getPrice())
                .isEqualTo(productEntity.getPrice().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).doubleValue());
        assertThat(productCard.getImageUrl()).isEqualTo(productEntity.getMedia().getPath());
        assertThat(productCard.getDescription()).isEqualTo(productEntity.getDescription());
    }

    @Test
    public void checkConvertProductEntityToProductDto_descriptionIsNull() {
        MediaEntity mediaEntity = MediaEntity.builder()
                .id(2L)
                .path("/images/some-image-2.jpg")
                .build();
        ProductEntity productEntity = ProductEntity.builder()
                .id(2L)
                .title("Test title")
                .rating(4.3)
                .reviewsCount(10)
                .price(new BigDecimal(50001))
                .media(mediaEntity)
                .description(null)
                .build();

        ProductCard productCard = productMapper.toDto(productEntity);

        assertThat(productCard.getTitle()).isEqualTo(productEntity.getTitle());
        assertThat(productCard.getRating()).isEqualTo(productEntity.getRating());
        assertThat(productCard.getReviewsCount()).isEqualTo(productEntity.getReviewsCount());
        assertThat(productCard.getPrice())
                .isEqualTo(productEntity.getPrice().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).doubleValue());
        assertThat(productCard.getImageUrl()).isEqualTo(productEntity.getMedia().getPath());
        assertThat(productCard.getDescription()).isEqualTo("");
    }
}
