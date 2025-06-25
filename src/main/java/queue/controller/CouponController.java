package queue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import queue.service.CouponService;
import queue.service.QueueService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController {
    private final QueueService queueService;

    @PostMapping("/coupon")
    public ResponseEntity<String> requestCoupon(@RequestParam long userId) {
        if (!queueService.joinQueue(userId))
            return ResponseEntity.badRequest().body("이미 참여 중이거나 발급 완료");
        return ResponseEntity.ok("대기열 등록 – /api/queue/" + userId + " SSE 구독");
    }
}