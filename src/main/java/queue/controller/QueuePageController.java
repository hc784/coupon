package queue.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 대기열 화면(Thymeleaf "queue.html")을 반환하는 페이지 컨트롤러.
 *
 * <p>로그인 기반이 아니라 간단히 <code>userId</code>를 URL Path 로 받는다.
 *    예) <code>GET /queue/42</code>  → queue.html 렌더 + model.userId = 42</p>
 *
 * <p>실제 서비스에서 JWT·세션 인증을 쓰면
 *    <code>@AuthenticationPrincipal</code> 으로 교체해도 된다.</p>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/queue")
public class QueuePageController {

    @GetMapping("/{userId}")
    public String queuePage(@PathVariable long userId, Model model) {
        model.addAttribute("userId", userId);
        return "queue"; // resources/templates/queue.html
    }
}
