package org.example.jwt_test.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// @RestControllerAdvice: 모든 @RestController에서 발생하는 예외를
// 컨트롤러마다 따로 처리하지 않고 여기 한 곳에서 공통으로 처리하게 해줌
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @RequestBody 뒤에 @Valid를 붙인 DTO(예: LoginRequestDto)의
    // 필드 검증(@NotEmpty 등)이 실패하면 여기가 자동으로 실행됨
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        Map<String, Object> body = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        // 검증 실패한 필드가 여러 개일 수 있으므로 하나씩 꺼내서
        // "필드명 : 실패 메시지" 형태로 errors 맵에 담음
        for (FieldError fe : e.getBindingResult().getFieldErrors()){
            errors.put(fe.getField(), fe.getDefaultMessage());
        }

        body.put("message", "요청 본문 검증에 실패했습니다.");
        body.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e){
        Map<String, Object> body = new HashMap<>();
        body.put("message",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
