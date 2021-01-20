package ru.awg.rupost.demo.service;

import ru.awg.rupost.ProductCard;
import ru.awg.rupost.demo.exception.RecordNotFoundException;

import java.util.List;

public interface ProductService {

    ProductCard getProductById(long id) throws RecordNotFoundException;

    List<ProductCard> getProducts();
}
