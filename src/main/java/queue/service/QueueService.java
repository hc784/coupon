package queue.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final StringRedisTemplate redis;
    @Value("${coupon.queue-key:coupon:queue}")  private String queueKey;
    @Value("${coupon.seq-key:coupon:seq}")      private String seqKey;
    @Value("${coupon.issued-key-prefix}")       private String issuedPrefix;

    /** 대기열 등록, 이미 등록 / 발급 시 false */
    public boolean joinQueue(long userId) {
        // ① 중복 발급 방지 (1일 TTL)
        Boolean first = redis.opsForValue()
                             .setIfAbsent(issuedPrefix + userId, "1", Duration.ofDays(1));
        if (Boolean.FALSE.equals(first)) return false;

        // ② 전역 시퀀스 → ZSET score
        Long seq = redis.opsForValue().increment(seqKey);          // 원자적 INCR
        redis.opsForZSet().add(queueKey, String.valueOf(userId), seq);
        return true;
    }

    /** 현재 순위(1-based). 존재하지 않으면 null */
    public Long currentPosition(long userId) {
        Long rank = redis.opsForZSet().rank(queueKey, String.valueOf(userId));
        return rank == null ? null : rank + 1;                     // 0-based → 1-based
    }

    /** 큐에서 제거 (발급 완료·취소 시) */
    public void removeFromQueue(long userId) {
        redis.opsForZSet().remove(queueKey, String.valueOf(userId));
    }
}
