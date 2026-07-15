package org.example.jwt_test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.jwt_test.entity.CartItem;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {
    private Long id;
    private String productName;
    private int price;
    private int stock;

    public static CartItemResponseDto from(CartItem cartItem){
        return CartItemResponseDto.builder()
                .id(cartItem.getId())
                .productName(cartItem.getProduct().getProductName())
                .price(cartItem.getProduct().getPrice())
                .stock(cartItem.getStock())
                .build();
    }
}
