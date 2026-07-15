package org.example.jwt_test.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) // Application 선언 필수
@Table(name = "Users")
@NoArgsConstructor // JPA를 사용하기 위해서 필요
@AllArgsConstructor // Build를 사용하기 위해 필요
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,  nullable = false,  length = 50)
    private String username;
    @Column(nullable = false,  length = 100)
    private String password;
    @Column(nullable = false,  length = 50)
    private String name;
    @Column(nullable = false,  length = 100)
    private String email;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // User(회원)와 Role(역할)의 관계를 다대다로 설정
    // fetch = EAGER: User를 조회할 때마다 연결된 Role 가져옴
    @ManyToMany(fetch = FetchType.EAGER)
    // User-Role 관계를 저장할 중간(연결) 테이블
    @JoinTable(
            name = "user_roles",                        // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "user_id"), // 회원(User)
            inverseJoinColumns = @JoinColumn(name = "role_id") // 역할(Role)
    )

    // Set<Role> : 같은 Role이 중복으로 들어갈 수 없음 (단, 서로 다른 Role은 여러 개 가질 수 있음)
    // HashSet : Set의 구현체 중 하나, 해시(hash)값을 이용해 추가/중복 확인을 빠르게 처리
    // new HashSet<>() : 빈 Set 객체로 초기화 (null 방지)
    Set<Role> roles = new HashSet<>();
}
