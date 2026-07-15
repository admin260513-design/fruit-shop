package org.example.jwt_test.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_list")
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    // private String username;  만들기 전에는 이렇게 사용
    private User user;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private int stock;
    @Column(nullable = false)
    private int price;
    @Column
    private String img_url;
    @Column
    private String dece;
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(updatable = true)
    @LastModifiedDate
    private LocalDateTime updateAt;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProductStatus productStatus = ProductStatus.sell;
}
