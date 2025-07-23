package foodOrder.coupon.entitiy;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import foodOrder.shop.entitiy.Shop;
@Entity
@Table(name = "coupon_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CouponType {
    @Id @GeneratedValue
    private Long id;

    @Column(length = 50)
    private String name;           // “SUMMER2025” 등

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "shop_id") // nullable = true
    private Shop shop;             // null 이면 “전체 적용” 쿠폰

    private String description;
    private int discountPercent;
    
    @Column(nullable = false)
    private int validDays;   
    // 유효기간, 최대할인금액 추가.
}
