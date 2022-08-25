package com.codesoom.assignment;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.UserRegisterData;

import java.util.Map;

/**
 * 고양이 장난감 가게 서비스 테스트에 사용되는 데이터 저장소입니다.
 */
public class Fixture {
    public static final String SESSION_PATH = "/session";
    public static final Map<String, String> LOGIN_DATA_MAP = Map.of(
            "email", Fixture.EMAIL,
            "password", Fixture.PASSWORD
    );
    public static final Map<String, String> ADMIN_LOGIN_DATA_MAP = Map.of(
            "email", Fixture.ADMIN_EMAIL,
            "password", Fixture.PASSWORD
    );

    public static final String USER_PATH = "/users";
    public static final String EMAIL = "qjawlsqjacks@naver.com";
    public static final String PASSWORD = "1234";
    public static final String USER_NAME = "박범진";
    public static final String ROLE_USER = "USER";
    public static final String ADMIN_EMAIL = "admin@naver.com";
    public static final String ADMIN_NAME = "관리자";
    public static final Map<String, String> USER_DATA_MAP = Map.of(
            "email", EMAIL,
            "password", PASSWORD,
            "name", USER_NAME
    );
    public static final UserRegisterData USER_REGISTER_DATA = new UserRegisterData(
            EMAIL, PASSWORD, USER_NAME
    );
    public static final User USER = User.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .name(USER_NAME)
            .build();
    public static final UserRegisterData ADMIN_REGISTER_DATA = new UserRegisterData(
            ADMIN_EMAIL, PASSWORD, ADMIN_NAME
    );
    public static final User ADMIN = User.builder()
            .email(ADMIN_EMAIL)
            .password(PASSWORD)
            .name(ADMIN_NAME)
            .build();

    public static final String PRODUCT_PATH = "/products";
    public static final String PRODUCT_NAME = "상품명";
    public static final int QUANTITY = 10;
    public static final Integer PRICE = 10000;
    public static Product makeProduct(User user) {
        return Product.builder()
                .owner(user)
                .name(PRODUCT_NAME)
                .quantity(QUANTITY)
                .price(PRICE)
                .build();
    }
    public static Map<String, Object> productData(String userId) {
        return Map.of(
                "userId", userId,
                "name", Fixture.PRODUCT_NAME,
                "quantity", Fixture.QUANTITY,
                "price", Fixture.PRICE
        );
    }

    public static final String MODIFIED_PRODUCT_NAME = "변경된 상품명";
    public static final String MODIFIED_PRODUCT_DESCRIPTION = "변경된 설명";
    public static final int MODIFIED_PRODUCT_QUANTITY = 5;
    public static final int MODIFIED_PRODUCT_PRICE = 2550000;
    public static final Map<String, Object> PRODUCT_UPDATE_DATA = Map.of(
            "name", MODIFIED_PRODUCT_NAME,
            "description", MODIFIED_PRODUCT_DESCRIPTION,
            "quantity", MODIFIED_PRODUCT_QUANTITY,
            "price", MODIFIED_PRODUCT_PRICE
    );
    public static final ProductData PRODUCT_DATA = ProductData.builder()
            .name(MODIFIED_PRODUCT_NAME)
            .description(MODIFIED_PRODUCT_DESCRIPTION)
            .quantity(MODIFIED_PRODUCT_QUANTITY)
            .price(MODIFIED_PRODUCT_PRICE)
            .build();

    private Fixture() {
        throw new IllegalStateException("테스트 Util 클래스입니다.");
    }
}
