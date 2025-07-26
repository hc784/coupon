package foodOrder.coupon.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import foodOrder.coupon.entity.CouponType;

import java.util.Optional;

public interface CouponTypeRepository extends JpaRepository<CouponType, Long> {
    /**
     * coupon code로 CouponType 조회
     * @param code 쿠폰 코드
     * @return Optional<CouponType>
     */
    Optional<CouponType> findByCode(String code);
}