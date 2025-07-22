package coupon.queue.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import coupon.queue.service.QueueService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QueuePollingController {

    private final QueueService queueService;

    /**
     * 현재 대기열 순서 조회 (1-based).
     * - pos == null : 이미 큐에서 빠졌거나 발급 완료
     * - pos == 1    : 바로 발급 차례
     */
    @GetMapping("/queue/{userId}")
    public ResponseEntity<QueuePositionResponse> getPosition(@PathVariable long userId) {

        Long pos = queueService.currentPosition(userId);
        QueuePositionResponse body = (pos == null)
                ? QueuePositionResponse.done()
                : QueuePositionResponse.waiting(pos);

        return ResponseEntity.ok(body);
    }

    /** 간단한 응답 DTO (record는 Java 16+) */
    public record QueuePositionResponse(String status, Long position) {
        static QueuePositionResponse waiting(Long pos) {
            return new QueuePositionResponse("WAITING", pos);
        }
        static QueuePositionResponse done() {
            return new QueuePositionResponse("DONE", null);
        }
    }
}
