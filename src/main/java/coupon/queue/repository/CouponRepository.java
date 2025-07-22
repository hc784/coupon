package coupon.queue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import coupon.queue.entitiy.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByUserId(Long userId);
}
