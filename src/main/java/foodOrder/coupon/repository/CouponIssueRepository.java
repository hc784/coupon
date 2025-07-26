package foodOrder.coupon.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import foodOrder.coupon.entity.CouponIssue;

public interface CouponIssueRepository extends JpaRepository<CouponIssue, Long> {
    // 필요한 추가 메서드가 있으면 정의
}