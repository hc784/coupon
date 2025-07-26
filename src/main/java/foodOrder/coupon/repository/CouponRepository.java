package foodOrder.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import foodOrder.coupon.entity.CouponIssue;

public interface CouponRepository extends JpaRepository<CouponIssue, Long> {
    boolean existsByUserId(Long userId);
}
