package com.codesoom.assignment.controllers;

import com.codesoom.assignment.Fixture;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductController 의")
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    private <T> Map<String, String> postRequest(Map<String, T> data, String path) throws Exception {
        return objectMapper.readValue(mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<>() {
        });
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("register 메서드는")
    class Describe_register {
        @Nested
        @DisplayName("상품 정보와 유효한 토큰이 주어지면")
        class Context_with_productDataAndValidToken {
            private String accessToken;
            private String userId;

            @BeforeEach
            void prepare() throws Exception {
                Map<String, String> createdUser = postRequest(Fixture.USER_DATA_MAP, Fixture.USER_PATH);
                userId = createdUser.get("id");

                accessToken = postRequest(Fixture.LOGIN_DATA_MAP, Fixture.SESSION_PATH).get("accessToken");
            }

            @Test
            @DisplayName("상품을 생성하고 상품 조회 정보와 201을 응답한다")
            void It_returns_productInfo() throws Exception {
                mockMvc.perform(post(Fixture.PRODUCT_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(Fixture.productData(userId)))
                                .header("Authorization", "Bearer " + accessToken))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.userId").value(userId))
                        .andExpect(jsonPath("$.name", Is.is(Fixture.PRODUCT_NAME)))
                        .andExpect(jsonPath("$.quantity").value(Fixture.QUANTITY))
                        .andExpect(jsonPath("$.price").value(Fixture.PRICE));
            }
        }

        @Nested
        @DisplayName("상품 정보와 유효하지 않은 토큰이 주어지면")
        class Context_with_productDataAndInvalidToken {
            private String invalidToken;
            private String userId;

            @BeforeEach
            void prepare() throws Exception {
                Map<String, String> createdUser = postRequest(Fixture.USER_DATA_MAP, Fixture.USER_PATH);
                userId = createdUser.get("id");

                invalidToken = postRequest(Fixture.LOGIN_DATA_MAP, Fixture.SESSION_PATH)
                        .get("accessToken") + "0";
            }

            @Test
            @DisplayName("상품을 생성하고 상품 조회 정보와 201을 응답한다")
            void It_returns_productInfo() throws Exception {
                mockMvc.perform(post(Fixture.PRODUCT_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(Fixture.productData(userId)))
                                .header("Authorization", "Bearer " + invalidToken))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        @Nested
        @DisplayName("토큰이 주어지지 않으면")
        class Context_without_authorization {
            Long productId;

            @BeforeEach
            void prepare() {
                User user = userRepository.save(Fixture.USER);
                Product product = productRepository.save(Fixture.makeProduct(user));
                productId = product.getId();
            }

            @Test
            @DisplayName("예외 메시지와 401을 응답한다")
            void It_respond_unAuthorization() throws Exception {
                mockMvc.perform(put(Fixture.USER_PATH + "/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Fixture.PRODUCT_UPDATE_DATA)))
                        .andExpect(status().isUnauthorized());
            }
        }
    }
}
