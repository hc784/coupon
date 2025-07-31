package foodOrder.auth.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 루트(/), /login, /sign‑up 접속 시 모두 auth.html 반환
    @GetMapping({ "/", "/login", "/sign-up" })
    public String authPage() {
        return "auth";           // templates/auth.html
    }

    // 필요하다면 별도 경로를 추가해서 분리할 수도 있음
    // @GetMapping("/auth") public String auth() { return "auth"; }
}