package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.TokenGenerator;
import com.codesoom.assignment.controller.ControllerTest;
import com.codesoom.assignment.domain.products.Product;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.codesoom.assignment.ConstantsForTest.INVALID_TOKENS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("ProductDeleteController 클래스")
public class ProductDeleteControllerMockMvcTest extends ControllerTest {

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

    @BeforeEach
    void setup() {
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

    @DisplayName("delete 메서드는")
    @Nested
    class Describe_delete {

        @DisplayName("찾을 수 있는 상품의 id가 주어지면")
        @Nested
        class Context_with_exist_id {

            private String TOKEN;
            private Long EXIST_ID;

            @BeforeEach
            void setup() throws Exception {
                saveUser();
                this.TOKEN = TokenGenerator.generateToken(mockMvc, EMAIL, PASSWORD);
                final Product product
                        = Product.withoutId("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "");
                this.EXIST_ID = repository.save(product).getId();
            }

            @DisplayName("해당 상품을 삭제한다.")
            @Test
            void it_delete_product() throws Exception {
                mockMvc.perform(delete("/products/" + EXIST_ID)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN))
                        .andExpect(status().isNoContent());
            }
        }

        @DisplayName("찾을 수 없는 상품의 id가 주어지면")
        @Nested
        class Context_with_not_exist_id {

            private String TOKEN;
            private final Long NOT_EXIST_ID = 100L;

            @BeforeEach
            void setup() throws Exception {
                saveUser();
                this.TOKEN = TokenGenerator.generateToken(mockMvc, EMAIL, PASSWORD);
                if (repository.existsById(NOT_EXIST_ID)) {
                    repository.deleteById(NOT_EXIST_ID);
                }
            }

            @DisplayName("404 not found를 응답한다.")
            @Test
            void will_response_404_not_found() throws Exception {
                mockMvc.perform(delete("/products/" + NOT_EXIST_ID)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + TOKEN))
                        .andExpect(status().isNotFound());
            }
        }

        @DisplayName("유효하지 않은 토큰이 주어지면")
        @Nested
        class Context_with_invalid_token {

            private Long EXIST_ID;

            @BeforeEach
            void setup() {
                final Product product
                        = Product.withoutId("쥐돌이", "캣이즈락스타", BigDecimal.valueOf(4000), "");
                this.EXIST_ID = repository.save(product).getId();
            }

            @DisplayName("401 unauthorized를 응답한다.")
            @Test
            void it_delete_product() throws Exception {
                for (int i = 0; i < INVALID_TOKENS.size(); i++) {
                    mockMvc.perform(delete("/products/" + EXIST_ID)
                                    .header(HttpHeaders.AUTHORIZATION, INVALID_TOKENS.get(i)))
                            .andExpect(status().isUnauthorized());
                }
            }
        }
    }
}
