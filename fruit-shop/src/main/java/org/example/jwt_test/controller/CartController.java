package org.example.jwt_test.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jwt_test.dto.CartItemRequestDto;
import org.example.jwt_test.entity.CartItem;

import org.example.jwt_test.security.CustomUserDetails;
import org.example.jwt_test.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    // 1. 장바구니 상품 담기 (로그인 필요)
    // - product와 stock 값으로 상품 담기
    @PostMapping
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @Valid @RequestBody CartItemRequestDto dto){
        CartItem cartItem = cartService.addToCart(
                userDetails.getUsername(),dto.getProductId(), dto.getStock()
        );
        return ResponseEntity.ok(cartItem);
    }

    // 2. 장바구니 조회 (로그인 필요)
    @GetMapping
    public ResponseEntity<?> getCartItem(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<CartItem> cartItems = cartService.getCartItem(userDetails.getUsername());
        return ResponseEntity.ok(cartItems);
    }

    // 3. 장바구니 상품 수량 변경
    @PatchMapping("/{cartItemId}")
    public ResponseEntity<?> updateStock(@PathVariable Long cartItemId,
                                         @RequestParam int stock){
        CartItem cartItem = cartService.updateStock(cartItemId, stock);
        return ResponseEntity.ok(cartItem);
    }

    // 4. 장바구니 상품 삭제
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long cartItemId){
        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}