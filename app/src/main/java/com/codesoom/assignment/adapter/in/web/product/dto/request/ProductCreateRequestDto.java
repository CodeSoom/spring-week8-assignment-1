package com.codesoom.assignment.adapter.in.web.product.dto.request;

import com.codesoom.assignment.adapter.in.web.product.dto.ProductMapper;
import com.codesoom.assignment.product.application.port.command.ProductCreateRequest;
import com.codesoom.assignment.product.domain.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCreateRequestDto implements ProductCreateRequest {

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @NotBlank(message = "메이커를 입력하세요")
    private String maker;

    @NotNull(message = "가격을 입력하세요")
    @Min(value = 0, message = "가격은 0원 이상으로 입력하세요")
    private Integer price;

    private String imageUrl;

    @Builder
    private ProductCreateRequestDto(final String name, final String maker,
                                    final Integer price, final String imageUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Override
    public Product toEntity() {
        return ProductMapper.INSTANCE.toEntity(this);
    }
}
