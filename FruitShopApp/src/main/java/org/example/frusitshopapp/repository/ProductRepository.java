package org.example.frusitshopapp.repository;

import org.example.frusitshopapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContaining(String name);
}
