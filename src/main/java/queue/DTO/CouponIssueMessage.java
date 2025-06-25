package queue.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CouponIssueMessage {
    private Long userId;
    private String eventId;    // “summer-sale-2025” 등
}
