package org.example.jwt_test.config;

import org.example.jwt_test.exception.CustomAuthenticationEntryPoint;
import org.example.jwt_test.filter.JwtAuthenticationFilter;
import org.example.jwt_test.util.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// @EnableWebSecurity: Spring Security 기능을 활성화하겠다는 선언
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 필터를 만들 때 필요한 도구들을 미리 주입받음
    // (JwtAuthenticationFilter, CustomAuthenticationEntryPoint 둘 다 이미 만들어둔 것)
    private final JwtTokenizer jwtTokenizer;
    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    // 기존에 있던 PasswordEncoder bean - 그대로 유지
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 이 프로젝트의 보안 규칙 전체를 정의하는 핵심 Bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // 어떤 경로는 인증 없이 열어두고, 나머지는 인증 필요하게 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/refreshToken", "/api/register").permitAll()  // 로그인/재발급/회원가입은 누구나 접근 가능
                        // Swagger를 위해 추가
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/error"
                        ).permitAll()  // Swagger 페이지 및 API 명세 문서도 인증 없이 접근 가능, /error도 열어서 진짜 에러가 보이게 함
                        .anyRequest().authenticated()                                     // 나머지는 전부 인증 필요
                )
                // 우리가 만든 JwtAuthenticationFilter를,
                // Spring Security 기본 필터(UsernamePasswordAuthenticationFilter)보다 먼저 실행되도록 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenizer), UsernamePasswordAuthenticationFilter.class)
                // 폼 로그인(기본 로그인 화면) 비활성화 - 우리는 JWT 방식만 쓰니까
                .formLogin(form -> form.disable())
                // CSRF 보호 비활성화 - JWT는 세션 기반이 아니라서 CSRF 공격 위험이 적음
                .csrf(csrf -> csrf.disable())
                // 세션을 아예 만들지 않음 (STATELESS) - JWT는 서버가 상태를 안 들고 있는 방식이라서
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 아래에서 만들 CORS 설정을 적용
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource)
                )
                // 인증 실패 시, 우리가 만든 CustomAuthenticationEntryPoint가 처리하도록 연결
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                );

        return http.build();
    }

    // CORS(다른 도메인에서의 요청 허용) 설정을 정의하는 Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);   // 쿠키(인증정보)를 포함한 요청을 허용할지 여부

        // 어떤 프론트엔드 주소(도메인)에서의 요청을 허용할지 지정
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:8080"
        ));

        configuration.setAllowedHeaders(List.of("*"));   // 모든 종류의 헤더 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // 허용할 HTTP 메서드
        configuration.setMaxAge(3600L);   // 이 CORS 설정을 브라우저가 캐싱해둘 시간(초)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);   // 모든 경로("/**")에 위 설정 적용

        return source;
    }
}