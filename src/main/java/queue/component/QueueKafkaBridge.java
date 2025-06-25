package queue.component;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import queue.DTO.CouponIssueMessage;
import queue.service.QueueService;

@Component
@RequiredArgsConstructor
public class QueueKafkaBridge {

    private final StringRedisTemplate redis;
    private final KafkaTemplate<String, CouponIssueMessage> kafka;
    private final QueueService queueService;

    @Value("${coupon.queue-key:coupon:queue}") private String queueKey;
    @Value("${coupon.topic}")                  private String topic;
    @Value("${coupon.stock-key}")              private String stockKey;

    /** 100ms 마다 큐 맨 앞을 전송 – 필요에 따라 주기/배치 조정 */
    @Scheduled(fixedDelay = 100)
    public void popAndSend() {
        // ① 재고가 0 이하면 중단
        String remainStr = redis.opsForValue().get(stockKey);
        if (remainStr == null || Long.parseLong(remainStr) <= 0) return;

        // ② 맨 앞 pop (Redis 6.2+ : ZPOPMIN 원자적)
        Set<ZSetOperations.TypedTuple<String>> popped =
                redis.opsForZSet().popMin(queueKey, 1);
        if (popped == null || popped.isEmpty()) return;

        long userId = Long.parseLong(popped.iterator().next().getValue());
        kafka.send(topic, new CouponIssueMessage(userId, "summer-sale-2025"));
    }
}
