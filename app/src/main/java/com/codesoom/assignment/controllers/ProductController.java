package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductCommandService;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.ProductInquiryInfo;
import com.codesoom.assignment.utils.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductCommandService productCommandService;

    public ProductController(ProductCommandService productCommandService) {
        this.productCommandService = productCommandService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductInquiryInfo register(@RequestBody ProductData data) {
        return productCommandService.register(data, SecurityUtil.getInfo());
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public ProductInquiryInfo update(
            @PathVariable("productId") Long productId,
            @RequestBody ProductData data) {
        return null;
    }
}
