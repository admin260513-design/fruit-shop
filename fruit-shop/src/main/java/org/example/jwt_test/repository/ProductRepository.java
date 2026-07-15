package org.example.jwt_test.repository;

import org.example.jwt_test.entity.Product;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);

    Optional<Object> findByProductName(String productName, Sort sort);
}
