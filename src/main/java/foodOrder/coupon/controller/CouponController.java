package foodOrder.coupon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import foodOrder.auth.security.CustomUserDetails;
import foodOrder.coupon.service.QueueService;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponController {

    private final QueueService queueService;

    @PostMapping("/coupon")
    public ResponseEntity<String> requestCoupon(@AuthenticationPrincipal CustomUserDetails user) {
        long userId = user.getId();                 // ← 로그인 계정 ID
        if (!queueService.joinQueue(userId))
            return ResponseEntity.badRequest().body("이미 참여 중이거나 발급 완료");

        return ResponseEntity.ok("대기열 등록 – /api/queue SSE 폴링");
    }
}
