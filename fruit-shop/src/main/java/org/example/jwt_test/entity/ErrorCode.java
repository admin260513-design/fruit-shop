package org.example.jwt_test.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("사용자가 없습니다."),
    DATA_NOT_FOUND("정보가 없습니다."),
    ALREADY_EXPIRED("유효기간이 지났습니다.");

    private final String desc;
}
