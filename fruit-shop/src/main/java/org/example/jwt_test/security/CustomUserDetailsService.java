package org.example.jwt_test.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt_test.entity.User;
import org.example.jwt_test.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.debug("사용자 인증 시도 :: {}",username);

        // username으로 User 조회, 없으면 예외 발생
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("{} 사용자를 찾을 수 없어요. ",username);
                    return new UsernameNotFoundException("사용자가 없습니다. ::" + username);
                    }
                );
        log.debug("사용자 정보 로드 완료");

        // User가 가진 Role 목록 → 권한 이름(String) 리스트로 변환
        List<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getRoleType().name())
                .collect(Collectors.toList());

        // Security가 이해하는 CustomUserDetails 객체로 변환해서 리턴
        return CustomUserDetails.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}
