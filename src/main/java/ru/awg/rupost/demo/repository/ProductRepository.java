package ru.awg.rupost.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.awg.rupost.demo.model.ProductEntity;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}