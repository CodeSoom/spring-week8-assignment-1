package com.codesoom.assignment.controllers;

import com.codesoom.assignment.Fixture;
import com.codesoom.assignment.application.ProductCommandService;
import com.codesoom.assignment.application.UserCommandService;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.ProductInquiryInfo;
import com.codesoom.assignment.security.UserAuthentication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserCommandService userCommandService;
    @Autowired
    private ProductCommandService productCommandService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    private Map<String, String> postRequest(String path, Map<String, String> data) throws Exception {
        return objectMapper.readValue(
                mockMvc.perform(post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), new TypeReference<>() {
                }
        );
    }

    @Test
    @DisplayName("주문 요청이 수행되면 상품의 재고가 줄어든다")
    void WhenOrderRequestIsFulfilledStockOfProductIsReduced() throws Exception {
        // given
        final User seller = userCommandService.register(Fixture.USER_REGISTER_DATA);
        final User buyer = userCommandService.register(Fixture.ADMIN_REGISTER_DATA);

        final ProductInquiryInfo productInfo = productCommandService.register(Fixture.PRODUCT_DATA,
                new UserAuthentication(seller.getId(), seller.getRole()));
        final int quantity = productInfo.getQuantity();

        final String accessToken = postRequest(Fixture.SESSION_PATH, Fixture.ADMIN_LOGIN_DATA_MAP).get("accessToken");
        // when
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "productId", productInfo.getId(),
                                "quantity", quantity - 1
                        )))
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isCreated());
        // then
        assertThat(productRepository.findById(productInfo.getId()).get().getQuantity())
                .isEqualTo(1);
    }
}
