package com.codesoom.assignment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestDocController {

    @GetMapping("/rest-doc")
    public String getRestDoc() {
        return "index";
    }

}
