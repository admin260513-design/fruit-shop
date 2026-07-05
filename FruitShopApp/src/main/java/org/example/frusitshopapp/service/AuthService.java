package org.example.frusitshopapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.frusitshopapp.dto.LoginRequestDto;
import org.example.frusitshopapp.dto.LoginResponseDto;
import org.example.frusitshopapp.dto.SignupRequestDto;
import org.example.frusitshopapp.entity.User;
import org.example.frusitshopapp.repository.UserRepository;
import org.example.frusitshopapp.util.JwtUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

//    1. 회원가입
    @Transactional
    public void signup(SignupRequestDto requestDto){

//        2. 아이디 중복확인
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()){
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

//        3. 비밀번호 암호화,저장
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);
    }

//    로그인
    //    1. 유저찾기
    public LoginResponseDto login(LoginRequestDto requestDto){
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(()->
                new UsernameNotFoundException("해당유저가 없습니다."));

//        2. 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

//        3.토큰 발급
        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponseDto(token);
    }

}
