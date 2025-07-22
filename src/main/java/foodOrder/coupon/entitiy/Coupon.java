package foodOrder.coupon.entitiy;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;          // 발급 받은 유저
    private String eventId;       // 이벤트 식별자(필요 시)
    private LocalDateTime issuedAt;

    public Coupon(Long userId, String eventId) {
        this.userId  = userId;
        this.eventId = eventId;
        this.issuedAt = LocalDateTime.now();
    }
}
