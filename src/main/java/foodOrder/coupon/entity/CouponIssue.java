package foodOrder.coupon.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import foodOrder.auth.entity.Users;



@Entity
@Table(name = "coupon_issues")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CouponIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private CouponType type;       // 어떤 종류 쿠폰인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;             // 받은 사용자 (User 엔티티 매핑)

    @Column(nullable = false)
    private LocalDateTime issuedAt; // 발급 시각
    @Column(nullable = false)
    private LocalDateTime expiresAt; // 사용자별 만료 시각
  
    public static CouponIssue of(CouponType type, Users user) {
        LocalDateTime now = LocalDateTime.now();
        return CouponIssue.builder()
                .type(type)
                .user(user)
                .issuedAt(now)
                .expiresAt(now.plusDays(type.getValidDays()))
                .build();
    }
}