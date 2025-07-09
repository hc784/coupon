package queue.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import queue.DTO.CouponIssueMessage;
import queue.service.QueueService;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QueueKafkaBridge {

    private final StringRedisTemplate redis;
    private final KafkaTemplate<String, CouponIssueMessage> kafka;
    private final QueueService queueService;

    @Value("${coupon.queue-key:coupon:queue}") private String queueKey;
    @Value("${coupon.topic}")                  private String topic;
    @Value("${coupon.stock-key}")              private String stockKey;

    /* ───────── Lua: 재고>0일 때만 ZPOPMIN + DECR ───────── */
    private static final DefaultRedisScript<String> POP_AND_DECR_SCRIPT;
    static {
        POP_AND_DECR_SCRIPT = new DefaultRedisScript<>();
        POP_AND_DECR_SCRIPT.setResultType(String.class);
        POP_AND_DECR_SCRIPT.setScriptText(
            // KEYS[1]=stockKey, KEYS[2]=queueKey
            "local stock = tonumber(redis.call('GET', KEYS[1]))         " +
            "if not stock or stock <= 0 then return nil end            " +
            "local popped = redis.call('ZPOPMIN', KEYS[2], 1)          " +
            "if popped[1] then                                         " +
            "  redis.call('DECR', KEYS[1])                             " +
            "  return popped[1]                                        " + // userId
            "end                                                       " +
            "return nil"
        );
    }
    /* ─────────────────────────────────────────────────────── */
    
    /** 100 ms마다 선착순 한 명을 Kafka로 전송 */
    @Scheduled(fixedDelay = 100)
    public void popAndSend() {
    	
        // ① Lua로 재고 확인+차감+큐 pop을 한 번에 실행
        String userIdStr = redis.execute(
            POP_AND_DECR_SCRIPT,
            List.of(stockKey, queueKey)          // KEYS
        );

        // ② 반환값이 없으면 (재고 0 | 큐 비어있음) 그대로 종료
        if (userIdStr == null || userIdStr.isEmpty()) return;

        // ③ Kafka 전송
        long userId = Long.parseLong(userIdStr);
        kafka.send(topic, new CouponIssueMessage(userId, "summer-sale-2025"));
    }
}
