package com.codesoom.assignment.product.application.port.command;

import com.codesoom.assignment.product.adapter.in.web.dto.response.ProductResponseDto;
import com.codesoom.assignment.product.domain.Product;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, // 빌드 시 구현체 생성 후 빈으로 등록합니다.
        injectionStrategy = InjectionStrategy.CONSTRUCTOR) // 생성자 주입 전략을 따릅니다.
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    /**
     * 상품 생성 RequestDto에서 상품 엔티티로 객체를 매핑합니다.
     *
     * @param productCreateRequest 등록할 상품 정보
     * @return 데이터가 매핑된 상품 엔티티
     */
    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductCreateRequest productCreateRequest);

    /**
     * 상품 수정 RequestDto에서 상품 엔티티로 객체를 매핑합니다.
     *
     * @param productUpdateRequest 수정할 상품 정보
     * @return 데이터가 매핑된 상품 엔티티
     */
    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductUpdateRequest productUpdateRequest);

    /**
     * 상품 엔티티에서 상품 정보 ResponseDto로 객체를 매핑합니다.
     *
     * @param entity 상품 정보
     * @return 데이터가 매핑된 상품 정보 ResponseDto 리턴
     */
    ProductResponseDto toResponse(Product entity);

    /**
     * 상품 엔티티 목록에서 상품 목록 ResponseDto로 객체를 매핑합니다.
     *
     * @param entity 상품 정보
     * @return 데이터가 매핑된 상품 목록 ResponseDto 리턴
     */
    List<ProductResponseDto> toResponseList(List<Product> entity);
}
