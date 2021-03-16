package com.codesoom.assignment.healthcheck.ui;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * health check를 위한 사용자 요청을 처리한다.
 */
@RestController
public class HelloController {
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
