package drack_company.demo.Telegram_bot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping(){
        return "i`m alive and traking task";
    }
}
