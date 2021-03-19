package com.codesoom.assignment.product.acceptance;

import com.codesoom.assignment.AcceptanceTest;
import com.codesoom.assignment.auth.dto.TokenResponseData;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.codesoom.assignment.product.acceptance.step.ProductAcceptanceStep.상품_목록_조회_요청;
import static com.codesoom.assignment.product.acceptance.step.ProductAcceptanceStep.상품_목록_포함됨;
import static com.codesoom.assignment.product.acceptance.step.ProductAcceptanceStep.상품_생성_요청;
import static com.codesoom.assignment.product.acceptance.step.ProductAcceptanceStep.상품_생성됨;
import static com.codesoom.assignment.product.acceptance.step.ProductAcceptanceStep.상품_응답됨;
import static com.codesoom.assignment.product.acceptance.step.ProductAcceptanceStep.상품_조회_목록_응답됨;
import static com.codesoom.assignment.product.acceptance.step.ProductAcceptanceStep.상품이_등록되어_있음;
import static com.codesoom.assignment.user.acceptance.step.UserAcceptanceStep.로그인_되어_있음;
import static com.codesoom.assignment.user.acceptance.step.UserAcceptanceStep.회원_등록되어_있음;

class ProductAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String NAME = "tester";

    private static String PRODUCT_NAME = "TOY";
    private static String MAKER = "CAT-OWNER";
    private static String IMAGE_URL = "https://http.cat/599";
    private static int PRICE = 5000;

    private static String PRODUCT_NAME2 = "TOY2";
    private static String MAKER2 = "CAT-OWNER2";
    private static String IMAGE_URL2 = "https://http.cat/600";
    private static int PRICE2 = 5000;

    private TokenResponseData tokenResponseData;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_등록되어_있음(EMAIL, PASSWORD, NAME);
        tokenResponseData = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() {
        ExtractableResponse<Response> response = 상품_생성_요청(tokenResponseData, PRODUCT_NAME, MAKER, IMAGE_URL, PRICE);

        상품_생성됨(response, PRODUCT_NAME, MAKER, IMAGE_URL, PRICE);
    }

    @DisplayName("등록된 상품 목록을 조회한다.")
    @Test
    void getProducts() {
        // given
        ExtractableResponse<Response> createResponse1 = 상품이_등록되어_있음(tokenResponseData, PRODUCT_NAME, MAKER, IMAGE_URL, PRICE);
        ExtractableResponse<Response> createResponse2 = 상품이_등록되어_있음(tokenResponseData, PRODUCT_NAME2, MAKER2, IMAGE_URL2, PRICE2);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_조회_목록_응답됨(response);
        상품_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void getProduct() {
        // given
        ExtractableResponse<Response> createResponse = 상품이_등록되어_있음(tokenResponseData, PRODUCT_NAME, MAKER, IMAGE_URL, PRICE);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_응답됨(response, createResponse);
    }
}

