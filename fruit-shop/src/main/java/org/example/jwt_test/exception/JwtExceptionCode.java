package org.example.jwt_test.exception;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum JwtExceptionCode {

    UNKNOWN_ERROR("UNKNOWN_ERROR", "알수없어요ㅠㅠ"),
    NOT_FOUND_TOKEN("NOT_FOUND_TOKEN","Headers에서 토큰 형식의 값을 찾지 못했어요."),
    INVALID_TOKEN("INVALID_TOKEN","유효하지 않은 토큰"),
    EXPIRED_TOKEN("EXPIRED_TOKEN","기간이 만료된 토큰"),
    UNSUPPORTED_TOKEN("UNSUPPORTED_TOKEN","지원하지 않는 토큰");

    JwtExceptionCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public static JwtExceptionCode findByCode(String code){
        return Arrays.stream(JwtExceptionCode.values())// enum 값 전체를 스트림으로
                .filter(c ->c.getCode().equals(code)) //code가 일치하는 것만
                .findFirst()                  // 그중 첫번째 것을 꺼냄
                .orElse(UNKNOWN_ERROR);         // 없으면 UNKNOWN_ERROR로 대체
    }


}
