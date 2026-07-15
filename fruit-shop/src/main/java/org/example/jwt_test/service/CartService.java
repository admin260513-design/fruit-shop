package org.example.jwt_test.service;

import lombok.RequiredArgsConstructor;
import org.example.jwt_test.entity.Cart;
import org.example.jwt_test.entity.CartItem;
import org.example.jwt_test.entity.Product;
import org.example.jwt_test.entity.User;
import org.example.jwt_test.repository.CartItemRepository;
import org.example.jwt_test.repository.CartRepository;
import org.example.jwt_test.repository.ProductRepository;
import org.example.jwt_test.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 장바구니에 상품 담기
    public CartItem addToCart(String username, Long productId, int stock){

        // 1. 로그인한 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 담으려는 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 3. 사용자의 장바주기 조회 (없으면 새로 생성)
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));

        // 4. 이미 담겨 있는 상품인지 확인 후 수량 갱신 또는 신규 추가
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()){
            // 4-1. 이미 담겨 있으면 기존 수량에 더하기
            CartItem cartItem = existingItem.get();
            cartItem.setStock(cartItem.getStock() + stock);
            return cartItemRepository.save(cartItem);
        }

        // 4-2. 없으면 새 항목으로 담기
        CartItem newItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .stock(stock)
                .build();
        return  cartItemRepository.save(newItem);
    }

    // 로그인한 사용자의 장바구니 항목 목록을 조회.
    public List<CartItem> getCartItem(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 없습니다."));

        return cartItemRepository.findByCart(cart);
    }

    // 장바구니에 담긴 특정 상품의 수량 변경
    public CartItem updateStock(Long cartItemId, int stock){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다."));

        cartItem.setStock(stock);
        return cartItemRepository.save(cartItem);
    }

    //  장바구니 상품 삭제
    public void removeItem(Long cartItemId){
        if (!cartItemRepository.existsById(cartItemId)){
            throw new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다.");
        }
         cartItemRepository.deleteById(cartItemId);
    }
}
