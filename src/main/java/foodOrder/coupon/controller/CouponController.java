package foodOrder.coupon.controller;

import foodOrder.auth.security.CustomUserDetails;
import foodOrder.coupon.entitiy.CouponType;
import foodOrder.coupon.repository.CouponTypeRepository;
import foodOrder.coupon.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final QueueService queueService;
    private final CouponTypeRepository typeRepo;

    /**
     * 쿠폰 신청 엔드포인트
     * POST /api/coupons/{typeId}/request
     */
    @PostMapping("/{typeId}/request")
    public ResponseEntity<String> requestCoupon(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long typeId
    ) {
        long userId = user.getId();

        /* ① 쿠폰 타입 존재 여부 검증 */
        CouponType type = typeRepo.findById(typeId)
                .orElse(null);
        if (type == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("해당 쿠폰 타입이 존재하지 않습니다.");
        }

        /* ② 가게 전용 쿠폰 권한 검증 (예시) */
        if (type.getShop() != null) {
            boolean allowed = isUserAllowedForShop(userId, type.getShop().getId());
            if (!allowed) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("이 가게 전용 쿠폰을 신청할 권한이 없습니다.");
            }
        }

        /* ③ 중복 발급 여부 + 대기열 등록 */
        boolean joined = queueService.joinQueue(userId, typeId);
        if (!joined) {
            return ResponseEntity.badRequest()
                    .body("이미 참여 중이거나 발급이 완료되었습니다.");
        }

        /* ④ 성공 응답 */
        return ResponseEntity.ok("대기열 등록 완료 – /api/queue 폴링 대기");
    }

    /* ------------------------------------------------------------
       예시 권한 체크: 실제 구현은 회원-가게 매핑 로직에 맞춰 변경하세요
       ------------------------------------------------------------ */
    private boolean isUserAllowedForShop(long userId, long shopId) {
        // (1) 사용자가 해당 가게 회원인지 DB 조회
        // (2) 관리자/점주 여부 또는 가입 여부 확인
        // 지금은 예시로 항상 true 반환
        return true;
    }
}
