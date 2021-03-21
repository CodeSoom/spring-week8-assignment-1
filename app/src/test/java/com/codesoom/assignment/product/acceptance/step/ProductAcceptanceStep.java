package com.codesoom.assignment.product.acceptance.step;

import com.codesoom.assignment.auth.dto.TokenResponseData;
import com.codesoom.assignment.product.domain.Product;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceStep {
    public static ExtractableResponse<Response> 상품이_등록되어_있음(TokenResponseData tokenResponseData,
                                                            String name, String maker, String imageUrl, int price) {
        return 상품_생성_요청(tokenResponseData, name, maker, imageUrl, price);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(TokenResponseData tokenResponseData,
                                                         String name, String maker, String imageUrl, int price) {
        Map<String, Object> params = new HashMap<>();

        params.put("name", name);
        params.put("maker", maker);
        params.put("imageUrl", imageUrl);
        params.put("price", price);

        return RestAssured.given().log().all().
                auth().oauth2(tokenResponseData.getAccessToken()).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/products").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract();
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response, String name, String maker, String imageUrl, int price) {
        Product product = response.as(Product.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getMaker()).isEqualTo(maker);
        assertThat(product.getImageUrl()).isEqualTo(imageUrl);
        assertThat(product.getPrice()).isEqualTo(price);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/products").
                then().
                log().all().
                extract();
    }

    public static void 상품_조회_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultProductIds = response.jsonPath().getList(".", Product.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> productIds = createdResponses.stream()
                .map(v -> v.path("id"))
                .map(v-> Long.valueOf(String.valueOf(v)))
                .collect(Collectors.toList());

        assertThat(resultProductIds).containsAll(productIds);
    }

    public static void 상품_응답됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(createdResponse.as(Product.class)).isNotNull();
    }

    public static void 상품_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}


