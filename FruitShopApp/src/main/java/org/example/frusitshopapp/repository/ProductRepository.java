package org.example.frusitshopapp.repository;

import org.example.frusitshopapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
