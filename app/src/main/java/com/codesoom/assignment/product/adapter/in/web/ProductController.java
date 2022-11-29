package com.codesoom.assignment.product.adapter.in.web;

import com.codesoom.assignment.product.adapter.in.web.dto.request.ProductCreateRequestDto;
import com.codesoom.assignment.product.adapter.in.web.dto.request.ProductUpdateRequestDto;
import com.codesoom.assignment.product.adapter.in.web.dto.response.CreateProductResponseDto;
import com.codesoom.assignment.product.adapter.in.web.dto.response.ProductResponseDto;
import com.codesoom.assignment.product.adapter.in.web.dto.response.UpdateProductResponseDto;
import com.codesoom.assignment.product.application.port.ProductUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    /**
     * 상품의 목록을 리턴합니다.
     *
     * @return 상품 목록 리턴
     */
    @GetMapping
    public List<ProductResponseDto> list() {
        return ProductResponseDto.fromList(
                productUseCase.getProducts()
        );
    }

    /**
     * 상품의 상세 정보를 리턴합니다.
     *
     * @param id 상품 고유 id
     * @return 상품 상세 정보 리턴
     */
    @GetMapping("{id}")
    public ProductResponseDto detail(@PathVariable final Long id) {
        return ProductResponseDto.from(
                productUseCase.getProduct(id)
        );
    }

    /**
     * 상품을 등록하고 리턴합니다.
     *
     * @param productCreateRequestDto 등록할 상품 정보
     * @return 등록한 상품 상세 정보 리턴
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public CreateProductResponseDto create(@RequestBody @Valid final ProductCreateRequestDto productCreateRequestDto) {
        return new CreateProductResponseDto(
                productUseCase.createProduct(productCreateRequestDto)
        );
    }

    /**
     * 상품을 수정하고 리턴합니다.
     *
     * @param id                      상품 고유 id
     * @param productUpdateRequestDto 수정할 상품 정보
     * @return 수정한 상품 상세 정보 리턴
     */
    @PatchMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public UpdateProductResponseDto update(@PathVariable final Long id,
                                           @RequestBody @Valid final ProductUpdateRequestDto productUpdateRequestDto) {
        return new UpdateProductResponseDto(
                productUseCase.updateProduct(id, productUpdateRequestDto)
        );
    }

    /**
     * 상품을 삭제합니다.
     *
     * @param id 상품 고유 id
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable final Long id) {
        productUseCase.deleteProduct(id);
    }
}
