package queue.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import queue.DTO.CouponIssueMessage;
import queue.entitiy.Coupon;
import queue.repository.CouponRepository;
import queue.service.QueueService;

@Component
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponRepository couponRepo;
    private final StringRedisTemplate redis;
    private final QueueService queueService;

    @Value("${coupon.stock-key}") private String stockKey;

    @KafkaListener(topics = "${coupon.topic}")
    public void consume(CouponIssueMessage msg) {
        Long remain = redis.opsForValue().decrement(stockKey);
        if (remain == null || remain < 0) {
            redis.opsForValue().increment(stockKey); // 롤백
            return;
        }
        couponRepo.save(new Coupon(msg.getUserId(), msg.getEventId()));
        queueService.removeFromQueue(msg.getUserId());              // 큐에서 제거
        redis.opsForValue().set("coupon:success:" + msg.getUserId(), "1");
    }
}

