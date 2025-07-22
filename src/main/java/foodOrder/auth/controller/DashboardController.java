package foodOrder.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    @GetMapping("/")
    public String showDashboard() {
        return "dashboard";
    }
}
