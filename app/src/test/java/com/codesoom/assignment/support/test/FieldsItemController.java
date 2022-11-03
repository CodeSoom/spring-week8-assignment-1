package com.codesoom.assignment.support.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FieldsItemController {

    @GetMapping("/test")
    public FieldsItemResponse test() {
        return new FieldsItemResponse();
    }
}
