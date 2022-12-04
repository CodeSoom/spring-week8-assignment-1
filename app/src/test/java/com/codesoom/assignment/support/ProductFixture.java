package com.codesoom.assignment.support;

import com.codesoom.assignment.adapter.in.web.product.dto.request.ProductCreateRequestDto;
import com.codesoom.assignment.adapter.in.web.product.dto.request.ProductUpdateRequestDto;
import com.codesoom.assignment.product.repository.Product;

import static com.codesoom.assignment.support.IdFixture.ID_MAX;

public enum ProductFixture {
    상품_1번(1L, "쥐돌이", "냥이월드", 5000, ""),
    상품_2번(2L, "범냐옹", "메이드인 안양", 3000000, "https://avatars.githubusercontent.com/u/59248326"),
    상품_이름_비정상(ID_MAX.value(), "", "이름이 없지롱", 200, ""),
    상품_메이커_비정상(ID_MAX.value(), "메이커가 없지롱", "", 200, ""),
    상품_가격_비정상(ID_MAX.value(), "가격이 음수지롱", "가격이 마이너스지롱", -20000, ""),
    ;

    private final Long id;
    private final String name;
    private final String maker;
    private final int price;
    private final String imageUrl;

    ProductFixture(Long id, String name, String maker, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product 엔티티_생성() {
        return 엔티티_생성(null);
    }

    public Product 엔티티_생성(final Long id) {
        return Product.builder()
                .id(id)
                .name(this.name)
                .maker(this.maker)
                .price(this.price)
                .imageUrl(this.imageUrl)
                .build();
    }

    public ProductCreateRequestDto 등록_요청_데이터_생성() {
        return ProductCreateRequestDto.builder()
                .name(this.name)
                .maker(this.maker)
                .price(this.price)
                .imageUrl(this.imageUrl)
                .build();
    }

    public ProductUpdateRequestDto 수정_요청_데이터_생성() {
        return ProductUpdateRequestDto.builder()
                .name(this.name)
                .maker(this.maker)
                .price(this.price)
                .imageUrl(this.imageUrl)
                .build();
    }

    public Long 아이디() {
        return this.id;
    }

    public String 이름() {
        return this.name;
    }

    public String 메이커() {
        return this.maker;
    }

    public int 가격() {
        return this.price;
    }

    public String 이미지_URL() {
        return this.imageUrl;
    }
}
