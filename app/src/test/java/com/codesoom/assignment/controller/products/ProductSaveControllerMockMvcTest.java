package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.TokenGenerator;
import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.domain.products.ProductDto;
import com.codesoom.assignment.domain.products.ProductRepository;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static com.codesoom.assignment.ConstantsForTest.INVALID_TOKENS;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@DisplayName("ProductSaveController 클래스")
public class ProductSaveControllerMockMvcTest extends ControllerTest {

    private final String EMAIL = "kimcs@codesoom.com";
    private final String PASSWORD = "rlacjftn098";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
        cleanup();
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
        repository.deleteAll();
    }

    User saveUser() {
        User user = User.of("김철수", EMAIL);
        user.changePassword(PASSWORD, passwordEncoder);

        return userRepository.save(user);
    }

    @DisplayName("saveProduct 메서드는")
    @Nested
    class Describe_save_product {

        @DisplayName("필수값을 모두 입력하면")
        @Nested
        class Context_with_valid_data {

            private final ProductDto VALID_PRODUCT_DTO
                    = new ProductDto("어쩌구", "어쩌구컴퍼니", BigDecimal.valueOf(2000), "url");
            private String TOKEN;

            @BeforeEach
            void setup() throws Exception {
                saveUser();
                this.TOKEN = TokenGenerator.generateToken(mockMvc, EMAIL, PASSWORD);
            }

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("상품을 성공적으로 등록한다.")
            @Test
            void it_will_save_product() throws Exception {
                mockMvc.perform(post("/products").accept(MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN)
                        .content(objectMapper.writeValueAsString(VALID_PRODUCT_DTO))
                        .contentType(MediaType.APPLICATION_JSON ))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(VALID_PRODUCT_DTO.getName())))
                        .andDo(document("post-products"
                                , requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰"))
                                , requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                                        fieldWithPath("maker").type(JsonFieldType.STRING).description("제조사"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                                .description("상품 이미지").optional()
                                )
                                , responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 식별자"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품명"),
                                        fieldWithPath("maker").type(JsonFieldType.STRING).description("제조사"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지")
                                )));
            }
        }

        @DisplayName("필수값을 하나라도 입력하지 않으면")
        @Nested
        class Context_with_invalid_data {

            private final ProductDto INVALID_PRODUCT_DTO
                    = new ProductDto("어쩌구", " ", BigDecimal.valueOf(2000), "url");
            private String TOKEN;

            @BeforeEach
            void setup() throws Exception {
                saveUser();
                this.TOKEN = TokenGenerator.generateToken(mockMvc, EMAIL, PASSWORD);
            }
            @DisplayName("400 bad request를 응답한다.")
            @Test
            void it_reponse_400_bad_request() throws Exception {
                mockMvc.perform(post("/products")
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN)
                        .content(objectMapper.writeValueAsString(INVALID_PRODUCT_DTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }

        @DisplayName("토큰이 주어지지 않으면")
        @Nested
        class Context_with_empty_authorization_header {

            private final ProductDto VALID_PRODUCT_DTO
                    = new ProductDto("어쩌구", "어쩌구컴퍼니", BigDecimal.valueOf(2000), "url");

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("401 unauthorized를 응답한다.")
            @Test
            void it_will_save_product() throws Exception {
                mockMvc.perform(post("/products").accept(MediaType.APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(VALID_PRODUCT_DTO))
                                .contentType(MediaType.APPLICATION_JSON ))
                        .andExpect(status().isUnauthorized());
            }
        }

        @DisplayName("유효하지 않은 토큰이 주어지면")
        @Nested
        class Context_with_invalid_token {

            private final ProductDto VALID_PRODUCT_DTO
                    = new ProductDto("어쩌구", "어쩌구컴퍼니", BigDecimal.valueOf(2000), "url");

            @AfterEach
            void cleanup() {
                repository.deleteAll();
            }

            @DisplayName("401 unauthorized를 응답한다.")
            @Test
            void it_will_save_product() throws Exception {
                for (int i = 0; i < INVALID_TOKENS.size(); i++) {
                    mockMvc.perform(post("/products").accept(MediaType.APPLICATION_JSON_UTF8)
                                    .header(HttpHeaders.AUTHORIZATION, INVALID_TOKENS.get(i))
                                    .content(objectMapper.writeValueAsString(VALID_PRODUCT_DTO))
                                    .contentType(MediaType.APPLICATION_JSON ))
                            .andExpect(status().isUnauthorized());
                }
            }
        }

    }

}
