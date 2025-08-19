package com.example.itview_spring.Controller.Content;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController  implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        // templates/error/customError.html 파일을 반환
        return "error/customError";
    }

    // 스프링 부트 2.3 이상에서는 getErrorPath() 메서드가 Deprecated라서
    // 별도 오버라이딩 안 해도 됩니다.
}
