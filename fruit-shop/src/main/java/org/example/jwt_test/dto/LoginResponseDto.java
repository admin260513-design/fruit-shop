package org.example.jwt_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Schema(description = "로그인 응답 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String username;
    private String name;
}
