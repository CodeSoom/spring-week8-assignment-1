package com.codesoom.assignment.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Test
 */
@RestController
public class HelloController {
    @RequestMapping("/")
    public String sayHello() {
        return "Hello, world!";
    }
}
