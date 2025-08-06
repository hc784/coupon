package foodOrder.coupon.controller;




import foodOrder.auth.security.CustomUserPrincipal;
import foodOrder.coupon.entity.CouponType;
import foodOrder.coupon.repository.CouponTypeRepository;
import foodOrder.coupon.service.CouponService;
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

    private final CouponService couponService;

    @PostMapping("/{typeId}/request")
    public ResponseEntity<Void> requestCoupon(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @PathVariable Long typeId
    ) {
        couponService.requestCoupon(user.getId(), typeId);
        return ResponseEntity.ok().build();   // 200 OK만 반환, 메시지는 필요 시 Body 추가
    }
}
