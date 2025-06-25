package queue.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import queue.DTO.CouponIssueMessage;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final StringRedisTemplate redis;
    private final KafkaTemplate<String, CouponIssueMessage> kafka;
    @Value("${coupon.topic}")      private String topic;
    @Value("${coupon.stock-key}")  private String stockKey;
    @Value("${coupon.issued-key-prefix}") private String issuedPrefix;

    public boolean enqueueIssue(Long userId) {
        // 1) 중복 발급 방지 (TTL: 1일)
        Boolean first = redis.opsForValue()
                             .setIfAbsent(issuedPrefix + userId, "1", Duration.ofDays(1));
        if (Boolean.FALSE.equals(first)) return false;

        // 2) Kafka enqueue
        kafka.send(topic, new CouponIssueMessage(userId, "summer-sale-2025"));
        return true;
    }
}
