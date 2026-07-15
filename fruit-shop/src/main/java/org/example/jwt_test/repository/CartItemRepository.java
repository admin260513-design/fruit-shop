package org.example.jwt_test.repository;

import org.example.jwt_test.entity.Cart;
import org.example.jwt_test.entity.CartItem;
import org.example.jwt_test.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
