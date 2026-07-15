package org.example.jwt_test.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.jwt_test.config.SecurityConfig;
import org.example.jwt_test.dto.RegisterRequestDto;
import org.example.jwt_test.entity.Role;
import org.example.jwt_test.entity.RoleType;
import org.example.jwt_test.entity.User;
import org.example.jwt_test.repository.RoleRepository;
import org.example.jwt_test.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class UserService {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 (비밀번호 인코딩 권한)
    @Transactional
    public User joinUser(RegisterRequestDto dto){
        User user = User.builder()
                .username(dto.getUsername())
                 // 평문 비밀번호를 BCrypt로 단방향 암호화하여 저장 (복호화 불가, 로그인 시 matches()로 비교)
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
        // Role의 대한 값을 채워줌
        // RoleType의 기본 값 "User"로 채워줌음
//        userRole 변수 안에 실제로 들어있는 것:
//        Role { id: 1, roleType: user }
        Role userRole = roleRepository.findByRoleType(RoleType.user).get();  // "user" 권한을 찾아옴
        user.setRoles(Collections.singleton(userRole));                       // 그 권한을 새 회원에게 부여

        return userRepository.save(user);
    }
    // username에 해당되는 user 체크
    public Boolean existsUser(String username){
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }
}
