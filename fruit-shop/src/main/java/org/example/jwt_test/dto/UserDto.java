package org.example.jwt_test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// 내 정보 조회를 서버로 이동 (Password 절대 금지)
public class UserDto {
    private Long id;
    private String username;
    private String name;
    private String email;
}
