package ru.awg.rupost.demo.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.awg.rupost.ProductCard;
import ru.awg.rupost.demo.dao.model.ProductEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    BigDecimal PENNY_DIVISOR = new BigDecimal(100);

    @Mapping(target = "price", source = "price", qualifiedByName = "toPrice")
    @Mapping(target = "imageUrl", source = "media.path")
    @Mapping(target = "description", source = "description", qualifiedByName = "toDescription")
    ProductCard toDto(ProductEntity product);

    @Named("toPrice")
    default double toPrice(BigDecimal price) {
        return price.divide(PENNY_DIVISOR, 2, RoundingMode.HALF_UP).doubleValue();
    }

    @Named("toDescription")
    default String toDescription(String description) {
        return Optional.ofNullable(description).orElse("");
    }
}
