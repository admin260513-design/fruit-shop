package org.example.jwt_test.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDto {
    @NotNull(message = "상품 id는 필수입니다.")
    private Long productId; // 담을 상품의 ID

    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 0보다 커야 합니다.")
    private Integer stock; // 담을 수량 (0보다 커야 함)
}
