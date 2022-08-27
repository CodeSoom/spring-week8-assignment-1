package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.OrderData;
import com.codesoom.assignment.dto.OrderInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @ResponseStatus(HttpStatus.CREATED)
    public OrderInfo create(@RequestBody OrderData orderData) {
        return null;
    }
}
