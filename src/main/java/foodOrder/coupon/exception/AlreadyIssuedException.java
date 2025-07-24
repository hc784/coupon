package foodOrder.coupon.exception;
public class AlreadyIssuedException extends RuntimeException {
    public AlreadyIssuedException(String msg) { super(msg); }
}
