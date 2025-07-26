package foodOrder.coupon.advice;

import foodOrder.coupon.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NotFoundException e) { return e.getMessage(); }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbidden(ForbiddenException e) { return e.getMessage(); }

    @ExceptionHandler(AlreadyIssuedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDup(AlreadyIssuedException e) { return e.getMessage(); }
}
