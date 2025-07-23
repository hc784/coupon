package foodOrder.coupon.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CouponIssueMessage {
    private Long userId;
    private Long typeId;    // 기존 eventId → CouponType PK(id)
}
