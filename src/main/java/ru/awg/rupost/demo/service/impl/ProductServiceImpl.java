package ru.awg.rupost.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.awg.rupost.ProductCard;
import ru.awg.rupost.demo.dao.ProductsDao;
import ru.awg.rupost.demo.exception.RecordNotFoundException;
import ru.awg.rupost.demo.service.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductsDao productsDao;

    @Override
    public ProductCard getProductById(long id) throws RecordNotFoundException {
        return productsDao.getProductById(id);
    }

    @Override
    public List<ProductCard> getProducts() {
        return productsDao.getProducts();
    }
}