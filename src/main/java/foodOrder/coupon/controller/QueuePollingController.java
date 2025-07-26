package foodOrder.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import foodOrder.auth.security.CustomUserDetails;
import foodOrder.coupon.service.QueueService;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QueuePollingController {

    private final QueueService queueService;

    @GetMapping("/queue/{typeId}")
    public ResponseEntity<QueuePositionResponse> getPosition(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long typeId) {

    	
        long userId = user.getId();
        Long pos = queueService.currentPosition(userId, typeId);

        QueuePositionResponse body = (pos == null)
                ? QueuePositionResponse.done()
                : QueuePositionResponse.waiting(pos);

        return ResponseEntity.ok(body);
    }


    public record QueuePositionResponse(String status, Long position) {
        static QueuePositionResponse waiting(Long pos) { return new QueuePositionResponse("WAITING", pos); }
        static QueuePositionResponse done()             { return new QueuePositionResponse("DONE", null); }
    }
}
