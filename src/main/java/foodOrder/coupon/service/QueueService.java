package foodOrder.coupon.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final StringRedisTemplate redis;
    @Value("${coupon.queue-key}")      private String baseQueueKey;
    @Value("${coupon.seq-key}")        private String baseSeqKey;
    @Value("${coupon.issued-key-prefix}")       private String issuedPrefix;

    /** 대기열 등록, 이미 등록 / 발급 시 false */
    public boolean joinQueue(long userId, long typeId) {
        // ① 중복 발급 방지 (1일 TTL)
        Boolean first = redis.opsForValue()
                             .setIfAbsent(issuedPrefix + userId, "1", Duration.ofDays(1));
        if (Boolean.FALSE.equals(first)) return false;

        // ② 전역 시퀀스 → ZSET score
        
        String seqKey   = baseSeqKey + ":" + typeId;
        String queueKey = baseQueueKey + ":" + typeId;
        Long seq = redis.opsForValue().increment(seqKey);
        redis.opsForZSet().add(queueKey, String.valueOf(userId), seq);
        return true;
    }

    /** 현재 순위(1-based). 존재하지 않으면 null */
    public Long currentPosition(long userId, long typeId) {
        String queueKey = baseQueueKey + ":" + typeId;
        Long rank = redis.opsForZSet().rank(queueKey, String.valueOf(userId));
        return (rank == null) ? null : rank + 1;
    }

    /** 큐에서 제거 (발급 완료·취소 시) */
    public void removeFromQueue(long userId, long typeId) {
        String queueKey = baseQueueKey + ":" + typeId;
        redis.opsForZSet().remove(queueKey, String.valueOf(userId));
    }
}
