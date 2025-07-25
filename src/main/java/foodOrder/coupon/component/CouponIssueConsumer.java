package foodOrder.coupon.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import foodOrder.auth.entity.User;
import foodOrder.auth.repository.UserRepository;
import foodOrder.coupon.DTO.CouponIssueMessage;
import foodOrder.coupon.entity.CouponIssue;
import foodOrder.coupon.entity.CouponType;
import foodOrder.coupon.repository.CouponIssueRepository;
import foodOrder.coupon.repository.CouponRepository;
import foodOrder.coupon.repository.CouponTypeRepository;
import foodOrder.coupon.service.QueueService;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final StringRedisTemplate redis;
    private final QueueService queueService;
    
    private final CouponTypeRepository typeRepo;
    private final CouponIssueRepository issueRepo;
    private final UserRepository userRepo;


    @Value("${coupon.stock-key}") private String stockKey;

    /* ───────── Lua 스크립트 (재고 차감 + 음수면 롤백 후 실패코드 -1 반환) ───────── */
    private static final DefaultRedisScript<Long> STOCK_DECR_SCRIPT;
    static {
        STOCK_DECR_SCRIPT = new DefaultRedisScript<>();
        STOCK_DECR_SCRIPT.setScriptText(
            // KEYS[1] = stockKey
            "local remain = redis.call('DECR', KEYS[1]) " +
            "if remain < 0 then " +
            "  redis.call('INCR', KEYS[1]); " +
            "  return -1; " +
            "end; " +
            "return remain;"
        );
        STOCK_DECR_SCRIPT.setResultType(Long.class);
    }
    /* ─────────────────────────────────────────────────────────────────────────── */

    @KafkaListener(topics = "${coupon.topic}")
    public void consume(CouponIssueMessage msg) {
    	
        /* ① 재고 차감(원자) */
        Long remain = redis.execute(
            STOCK_DECR_SCRIPT,
            Collections.singletonList(stockKey)
        );
        if (remain == null || remain < 0) return;   // 재고 부족 → 종료

        /* ② 발급 로직 (DB·큐·플래그) */
        try {
            // ② CouponType 조회
            CouponType type = typeRepo.findById(msg.getTypeId())
                .orElseThrow(() -> 
                    new IllegalArgumentException("Unknown coupon code: " + msg.getTypeId()));

            // ③ User 조회
            User user = userRepo.findById(msg.getUserId())
                .orElseThrow(() -> 
                    new IllegalArgumentException("No such user: " + msg.getUserId()));

            // ④ CouponIssue 생성 (issuedAt, expiresAt 자동 계산)
            CouponIssue issue = CouponIssue.of(type, user);
            issueRepo.save(issue);
            
            
            queueService.removeFromQueue(msg.getUserId());                         // ZSET 제거
            redis.opsForValue().set("coupon:success:" + msg.getUserId(), "1");     // 성공 플래그
        } catch (Exception e) {
            // ★ 예외 시 재고 복원: Lua 한 번 더 호출
            redis.opsForValue().increment(stockKey);
            throw e;
        }
    }
}
