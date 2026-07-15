package org.example.jwt_test.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "roles")
@Entity
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Role 권한 부여
    // Role 기본 값을 강제로 부여(방어 로직은 if로 추후에 공부 필요)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
