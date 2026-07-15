package org.example.jwt_test.repository;

import org.example.jwt_test.entity.Cart;
import org.example.jwt_test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
