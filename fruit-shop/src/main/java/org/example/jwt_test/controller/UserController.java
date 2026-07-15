package org.example.jwt_test.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt_test.dto.LoginRequestDto;
import org.example.jwt_test.dto.LoginResponseDto;
import org.example.jwt_test.dto.RegisterRequestDto;
import org.example.jwt_test.entity.RefreshToken;
import org.example.jwt_test.entity.User;
import org.example.jwt_test.service.RefreshTokenService;
import org.example.jwt_test.service.UserService;
import org.example.jwt_test.util.JwtTokenizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto dto){
        // 기존 회원가입 API -  그대로 유지
        // 요청 데이터를 서비스로 전달, 회원가입 처리 및 저장
        User savedUser = userService.joinUser(dto);
        // 생성된 사용자 id를 응답으로 반환
        return ResponseEntity.ok(savedUser.getId());
    }

    // 로그인 API - 새로 추가
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                                   BindingResult bindingResult,
                                   HttpServletResponse response){

        // 1. 입력값 자체가 유호한지 검증 (@NotEmpty 등)
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // 2. username으로 사용자 조회 + 비밀번호 일치 확인
        User user = userService.findByUsername(loginRequestDto.getUsername()).orElse(null);
        if (user == null || !passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디나 비밀번호가 올바르지 않습니다.");
        }

        // 3. Role 목록을 문자열 리스트로 변환 (토큰에 담기 위해)
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleType().name())
                .collect(Collectors.toList());

        // 4. Access Token + Refresh Token 발급
        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);

        // 5. Refresh Token을 DB에 저장 (나중에 재발급 시 대조용)
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUserId(user.getId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        // 6. 토큰을 쿠키에 담아도 응답 (HttpOnly - 자바스크림트로 접근 불가)
        addTokenCookie("accessToken", accessToken, jwtTokenizer.getAccessTokenExpireCount(), response);
        addTokenCookie("refreshToken", refreshToken, jwtTokenizer.getRefreshTokenExpireCount(), response);

        // 7. 응답 body에도 토큰 정보 포함
        LoginResponseDto requestDto = LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .build();
        return ResponseEntity.ok(requestDto);
   }

   // Access Token 재발급 API - 새로 추가
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response){

        // 1. 쿠키에서 Refresh Token 꺼내기
        String token = getRefreshTokenFromCookie(request);
        if (token == null) {
            return ResponseEntity.badRequest().body("리프레시토큰이 없어요.");
        }
            try{
                // 2. 토큰 검증 + 파싱
                Claims claims = jwtTokenizer.parseRefreshToken(token);

                // 3. DB에 저장된 토큰과 일치하는지 확인
                RefreshToken dbToken = refreshTokenService.findRefreshToken(token).orElse(null);
                if (dbToken == null || !token.equals(dbToken.getToken())){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 이상해요.");
                }

                // 4. 사용자 정보 다시 조회
                Long userId = claims.get("userId", Long.class);
                User user = userService.getUser(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못했습니다."));

                //  5. 새 Access Token만 재발급
                List<String> roles = (List<String>) claims.get("roles");
                String newAccessToken = jwtTokenizer.createAccessToken(userId, user.getEmail(), user.getName(), user.getUsername(), roles);

                // 6. 새 토큰을 쿠키로 갱신
                addTokenCookie("accessToken", newAccessToken, jwtTokenizer.getAccessTokenExpireCount(), response);

                LoginResponseDto responseDto = LoginResponseDto.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(token)
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .build();

                return ResponseEntity.ok(responseDto);
                } catch (MalformedJwtException | IllegalArgumentException e){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh Token");
                } catch (ExpiredJwtException e){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료됨.");
                } catch (Exception e) {
                    return ResponseEntity.internalServerError().body("Internal server error");
                }
            }
    // 쿠키에서 refreshToken 값을 꺼내는 헬퍼
    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 토큰을 HttpOnly 쿠키로 만들어 응답에 추가하는 헬퍼
    private void addTokenCookie(String cookieName, String cookieValue, Long expireCount, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(Math.toIntExact(expireCount / 1000));
        response.addCookie(cookie);
    }
}
