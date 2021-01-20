package ru.awg.rupost.demo.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.awg.rupost.ProductCard;
import ru.awg.rupost.demo.converter.ProductMapper;
import ru.awg.rupost.demo.exception.RecordNotFoundException;
import ru.awg.rupost.demo.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsDao {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductCard getProductById(long id) throws RecordNotFoundException {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new RecordNotFoundException(String.format("No product record exist for id: %d", id)));
    }

    public List<ProductCard> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}
