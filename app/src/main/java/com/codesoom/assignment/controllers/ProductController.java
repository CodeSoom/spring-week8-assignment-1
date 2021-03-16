// REST
// /products -> Create, Read
// /products/{id} -> Read, Update, Delete

package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductCreateData;
import com.codesoom.assignment.dto.ProductResultData;
import com.codesoom.assignment.dto.ProductUpdateData;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/** 상품에 대해 요청한다. */
@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 전체 상품 목록을 리턴한다.
     *
     * @return 저장되어 있는 전체 상품 목록
     */
    @GetMapping
    public List<ProductResultData> list() {
        return productService.getProducts();
    }

    /**
     * 주어진 식별자에 해당하는 상품을 리턴한다.
     *
     * @param id - 조회하고자 하는 상품의 식별자
     * @return 주어진 {@code id}에 해당하는 상품
     */
    @GetMapping("/{id}")
    public ProductResultData detail(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        return ProductResultData.of(product);
    }

    /**
     * 주어진 상품을 저장하고 해당 상품을 리턴한다.
     *
     * @param productCreateData - 저장하고자 하는 새로운 상품
     * @return 저장 된 상품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public ProductResultData create(@RequestBody @Valid ProductCreateData productCreateData) {
        return productService.createProduct(productCreateData);
    }

    /**
     * 주어진 식별자에 해당하는 상품을 수정하고 해당 상품을 리턴한다.
     *
     * @param id - 수정하고자 하는 상품의 식별자
     * @param productUpdateData - 수정하고자 하는 새로운 상품
     * @return 수정 된 상품
     */
    @PatchMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public ProductResultData update(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateData productUpdateData
    ) {
        return productService.updateProduct(id, productUpdateData);
    }

    /**
     * 주어진 식별자에 해당하는 상품을 삭제하고 삭제된 상품을 리턴한다.
     *
     * @param id - 삭제하고자 하는 상품의 식별자
     * @return 삭제 된 상품
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public ProductResultData delete(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}
