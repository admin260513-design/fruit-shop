package org.example.jwt_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청 DTO")
// 클라 > 서버로 데이터 전송
public class RegisterRequestDto {
    @Schema(description = "ID", example = "aaa")
    private String username;
    @Schema(description = "PW", example = "1234")
    private String password;
    @Schema(description = "Email", example = "aaa@test.com")
    private String email;
    @Schema(description = "이름", example = "김철수")
    private String name;
}
