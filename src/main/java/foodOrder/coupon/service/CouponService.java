package foodOrder.coupon.service;

import foodOrder.auth.entity.User;
import foodOrder.coupon.entity.CouponType;
import foodOrder.coupon.repository.CouponTypeRepository;
import foodOrder.coupon.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponTypeRepository typeRepo;
    private final QueueService queueService;

    /**
     * 쿠폰 신청 비즈니스 로직:
     * 1) 타입 존재 검사
     * 2) 권한 / 가게 전용 쿠폰 검증
     * 3) 중복 발급 / 대기열 등록
     */
    @Transactional
    public void requestCoupon(long userId, long typeId) {

        /* ① 존재 여부 */
        CouponType type = typeRepo.findById(typeId)
                .orElseThrow(() -> new NotFoundException("쿠폰 타입이 없습니다."));

        /* ② 권한 검증 (가게 전용 쿠폰 예시) */
        if (type.getShop() != null && !isUserAllowedForShop(userId, type.getShop().getId())) {
            throw new ForbiddenException("해당 가게 쿠폰을 신청할 권한이 없습니다.");
        }

        /* ③ 중복 발급 / 대기열 등록 */
        boolean joined = queueService.joinQueue(userId, typeId);
        if (!joined) {
            throw new AlreadyIssuedException("이미 신청했거나 발급 완료된 쿠폰입니다.");
        }
    }

    /* 실제 구현은 회원-가게 매핑 로직에 맞게 변경 */
    private boolean isUserAllowedForShop(long userId, long shopId) {
        // 예: ShopMembershipRepository.existsByUserIdAndShopId(userId, shopId)
        return true;
    }
}
