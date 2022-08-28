package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.OrderService;
import com.codesoom.assignment.dto.OrderData;
import com.codesoom.assignment.dto.OrderInfo;
import com.codesoom.assignment.utils.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderInfo create(@RequestBody OrderData orderData) {
        return OrderInfo.from(orderService.create(orderData, SecurityUtil.getInfo()));
    }
}
