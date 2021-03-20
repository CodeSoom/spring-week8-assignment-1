package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인사 관련 요청을 처리합니다.
 */
@RestController
public class HelloController {
    /**
     * 인사를 요청하면 인사말을 반환한다.
     * @return "Hello, world!"
     */
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
