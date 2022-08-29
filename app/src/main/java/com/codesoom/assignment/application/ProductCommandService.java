package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.ProductInquiryInfo;
import org.springframework.security.core.Authentication;

public interface ProductCommandService {
    /**
     * 상품을 생성하고 리턴한다.
     *
     * @param data 상품 정보
     * @param authentication 유저 정보
     * @return 상품
     */
    ProductInquiryInfo register(ProductData data, Authentication authentication);

    /**
     * 상품을 수정하고 리턴한다.
     *
     * @param productId 식별자
     * @param productData 변경할 상품 정보
     * @param authentication 유저 정보
     * @return 변경된 상품
     */
    Product update(Long productId, ProductData productData, Authentication authentication);
}
