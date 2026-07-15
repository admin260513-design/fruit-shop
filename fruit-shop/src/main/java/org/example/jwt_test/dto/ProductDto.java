package org.example.jwt_test.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    @NotEmpty(message = "상품명은 필수입니다.")
    private String productName;
    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 0보다 커야 합니다.")
    private int stock;
    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 0보다 커야 합니다.")
    private int price;
    private String img_url;
    private String dece;
}