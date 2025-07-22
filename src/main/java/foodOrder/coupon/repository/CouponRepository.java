package foodOrder.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import foodOrder.coupon.entitiy.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByUserId(Long userId);
}
