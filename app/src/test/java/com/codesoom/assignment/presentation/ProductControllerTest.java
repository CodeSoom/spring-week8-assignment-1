package com.codesoom.assignment.presentation;

import com.codesoom.assignment.MockMvcCharacterEncodingCustomizer;
import com.codesoom.assignment.common.utils.JsonUtil;
import com.codesoom.assignment.product.adapter.in.web.ProductController;
import com.codesoom.assignment.product.adapter.in.web.dto.request.ProductCreateRequestDto;
import com.codesoom.assignment.product.adapter.in.web.dto.request.ProductUpdateRequestDto;
import com.codesoom.assignment.product.application.ProductService;
import com.codesoom.assignment.product.application.port.command.ProductUpdateRequest;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.exception.ProductNotFoundException;
import com.codesoom.assignment.role.domain.Role;
import com.codesoom.assignment.session.application.AuthenticationService;
import com.codesoom.assignment.session.exception.InvalidTokenException;
import com.codesoom.assignment.support.AuthHeaderFixture;
import com.codesoom.assignment.support.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_값_비정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_정상_토큰;
import static com.codesoom.assignment.support.IdFixture.ID_1;
import static com.codesoom.assignment.support.IdFixture.ID_MAX;
import static com.codesoom.assignment.support.ProductFixture.상품_1번;
import static com.codesoom.assignment.support.ProductFixture.상품_2번;
import static com.codesoom.assignment.support.ProductFixture.상품_가격_비정상;
import static com.codesoom.assignment.support.ProductFixture.상품_메이커_비정상;
import static com.codesoom.assignment.support.ProductFixture.상품_이름_비정상;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(MockMvcCharacterEncodingCustomizer.class)
@DisplayName("ProductController 웹 유닛 테스트")
class ProductControllerTest {
    private static final String REQUEST_PRODUCT_URL = "/products";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    /**
     * Spring Boot 2.3 버전까지 mock이 각 테스트 종료 후 reset되지 않는 이슈 존재 <br>
     * 따라서 각 테스트 실행 전 mock의 invoke 횟수를 수동으로 초기화 시켜줍니다.
     * <br><br>
     * Ref: https://github.com/spring-projects/spring-boot/issues/12470
     */
    @BeforeEach
    void setUpClearMock() {
        Mockito.clearInvocations(productService);
    }

    @BeforeEach
    void setUpAuthToken() {
        given(authenticationService.parseToken(eq(유저_1번_값_비정상_토큰.토큰_값())))
                .willThrow(new InvalidTokenException(유저_1번_값_비정상_토큰.토큰_값()));


        given(authenticationService.parseToken(eq(유저_1번_정상_토큰.토큰_값())))
                .willReturn(유저_1번_정상_토큰.아이디());
        given(authenticationService.roles(eq(유저_1번_정상_토큰.아이디())))
                .willReturn(Arrays.asList(
                        new Role(유저_1번_정상_토큰.아이디(), "USER")
                ));
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class list_메서드는 {

        @Test
        @DisplayName("200 코드로 응답한다")
        void it_responses_200() throws Exception {
            ResultActions perform = mockMvc.perform(
                    get(REQUEST_PRODUCT_URL)
            );

            perform.andExpect(status().isOk());

            verify(productService).getProducts();
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class detail_메서드는 {

        @Nested
        @DisplayName("찾을 수 없는 id가 주어지면")
        class Context_with_not_exist_id {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(ID_MAX.value()))
                        .willThrow(new ProductNotFoundException());
            }

            @Test
            @DisplayName("404 코드로 응답한다")
            void it_responses_404() throws Exception {
                ResultActions perform = mockMvc.perform(
                        get(REQUEST_PRODUCT_URL + "/" + ID_MAX.value())
                );

                perform.andExpect(status().isNotFound());

                verify(productService).getProduct(ID_MAX.value());
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 id가 주어지면")
        class Context_with_exist_id {

            @BeforeEach
            void setUp() {
                given(productService.getProduct(ID_1.value()))
                        .willReturn(상품_1번.엔티티_생성(ID_1.value()));
            }

            @Test
            @DisplayName("200 코드로 응답한다")
            void it_responses_200() throws Exception {
                ResultActions perform = mockMvc.perform(
                        get("/products/" + ID_1.value())
                );

                perform.andExpect(status().isOk());

                verify(productService).getProduct(ID_1.value());
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class create_메서드는 {

        @Nested
        @DisplayName("유효하지 않은 인증 토큰이 주어지면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = 상품_등록_API_요청(
                        유저_1번_값_비정상_토큰,
                        상품_1번
                );

                perform.andExpect(status().isUnauthorized());

                verify(productService, never())
                        .createProduct(any(ProductCreateRequestDto.class));
            }
        }

        @Nested
        @DisplayName("인증 토큰이 없다면")
        class Context_with_not_exist_token {

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = mockMvc.perform(
                        post(REQUEST_PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValueAsString(상품_1번.등록_요청_데이터_생성()))
                );

                perform.andExpect(status().isUnauthorized());

                verify(productService, never())
                        .createProduct(any(ProductCreateRequestDto.class));
            }
        }

        @Nested
        @DisplayName("인증 토큰이 유효하고")
        class Context_with_valid_token {
            private final AuthHeaderFixture 유효한_인증_토큰 = 유저_1번_정상_토큰;

            @Nested
            @DisplayName("유효하지 않은 상품 정보가 주어지면")
            class Context_with_invalid_product {

                @Nested
                @DisplayName("상품명이 공백일 경우")
                class Context_with_empty_name {

                    @Test
                    @DisplayName("400 코드로 응답한다")
                    void it_responses_400() throws Exception {
                        ResultActions perform = 상품_등록_API_요청(
                                유효한_인증_토큰,
                                상품_이름_비정상
                        );

                        perform.andExpect(status().isBadRequest());

                        verify(productService, never())
                                .createProduct(any(ProductCreateRequestDto.class));
                    }
                }

                @Nested
                @DisplayName("메이커가 공백일 경우")
                class Context_with_empty_maker {

                    @Test
                    @DisplayName("400 코드로 응답한다")
                    void it_responses_400() throws Exception {
                        ResultActions perform = 상품_등록_API_요청(
                                유효한_인증_토큰,
                                상품_메이커_비정상
                        );

                        perform.andExpect(status().isBadRequest());

                        verify(productService, never())
                                .createProduct(any(ProductCreateRequestDto.class));
                    }
                }

                @Nested
                @DisplayName("가격이 0원 미만일 경우")
                class Context_with_negative_price {

                    @Test
                    @DisplayName("400 코드로 응답한다")
                    void it_responses_400() throws Exception {
                        ResultActions perform = 상품_등록_API_요청(
                                유효한_인증_토큰,
                                상품_가격_비정상
                        );

                        perform.andExpect(status().isBadRequest());

                        verify(productService, never())
                                .createProduct(any(ProductCreateRequestDto.class));
                    }
                }
            }

            @Nested
            @DisplayName("유효한 상품 정보가 주어지면")
            class Context_with_valid_product {

                @BeforeEach
                void setUp() {
                    given(productService.createProduct(eq(상품_1번.등록_요청_데이터_생성())))
                            .will(invocation -> {
                                ProductCreateRequestDto product = invocation.getArgument(
                                        0, ProductCreateRequestDto.class
                                );

                                return Product.builder()
                                        .id(상품_1번.아이디())
                                        .name(product.getName())
                                        .maker(product.getMaker())
                                        .price(product.getPrice())
                                        .imageUrl(product.getImageUrl())
                                        .build();
                            });
                }

                @Test
                @DisplayName("201 코드로 응답한다")
                void it_responses_201() throws Exception {
                    ResultActions perform = 상품_등록_API_요청(
                            유효한_인증_토큰,
                            상품_1번
                    );

                    perform.andExpect(status().isCreated());

                    verify(productService).createProduct(any(ProductCreateRequestDto.class));
                }
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class update_메서드는 {

        @Nested
        @DisplayName("유효하지 않은 인증 토큰이 주어지면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = 상품_수정_API_요청(
                        상품_2번.아이디(),
                        유저_1번_값_비정상_토큰,
                        상품_2번
                );

                perform.andExpect(status().isUnauthorized());

                verify(productService, never())
                        .updateProduct(any(Long.class), any(ProductUpdateRequest.class));
            }
        }

        @Nested
        @DisplayName("인증 토큰이 없다면")
        class Context_with_not_exist_token {

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = mockMvc.perform(
                        patch(REQUEST_PRODUCT_URL + "/" + 상품_2번.아이디())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.writeValueAsString(상품_2번.수정_요청_데이터_생성()))
                );

                perform.andExpect(status().isUnauthorized());

                verify(productService, never())
                        .updateProduct(any(Long.class), any(ProductUpdateRequest.class));
            }
        }

        @Nested
        @DisplayName("인증 토큰이 유효하고")
        class Context_with_valid_token {
            @Nested
            @DisplayName("찾을 수 없는 id가 주어지면")
            class Context_with_not_exist_id {
                private final Long 찾을_수_없는_id = ID_MAX.value();

                @BeforeEach
                void setUp() {
                    given(productService.updateProduct(eq(찾을_수_없는_id), any(ProductUpdateRequest.class)))
                            .willThrow(new ProductNotFoundException());
                }

                @Test
                @DisplayName("404 코드로 응답한다")
                void it_responses_404() throws Exception {
                    ResultActions perform = 상품_수정_API_요청(
                            찾을_수_없는_id,
                            유저_1번_정상_토큰,
                            상품_2번
                    );

                    perform.andExpect(status().isNotFound());

                    verify(productService).updateProduct(eq(찾을_수_없는_id), any(ProductUpdateRequest.class));
                }
            }

            @Nested
            @DisplayName("찾을 수 있는 id가 주어지고")
            class Context_with_exist_id {
                private final Long 찾을_수_있는_id = 상품_1번.아이디();

                @Nested
                @DisplayName("유효하지 않은 상품 정보가 주어지면")
                class Context_with_invalid_product {

                    @Nested
                    @DisplayName("상품명이 공백일 경우")
                    class Context_with_empty_name {

                        @Test
                        @DisplayName("400 코드로 응답한다")
                        void it_responses_400() throws Exception {
                            ResultActions perform = 상품_수정_API_요청(
                                    찾을_수_있는_id,
                                    유저_1번_정상_토큰,
                                    상품_이름_비정상
                            );

                            perform.andExpect(status().isBadRequest());

                            verify(productService, never())
                                    .updateProduct(any(Long.class), any(ProductUpdateRequest.class));
                        }
                    }

                    @Nested
                    @DisplayName("메이커가 공백일 경우")
                    class Context_with_empty_maker {

                        @Test
                        @DisplayName("400 코드로 응답한다")
                        void it_responses_400() throws Exception {
                            ResultActions perform = 상품_수정_API_요청(
                                    찾을_수_있는_id,
                                    유저_1번_정상_토큰,
                                    상품_메이커_비정상
                            );

                            perform.andExpect(status().isBadRequest());

                            verify(productService, never())
                                    .updateProduct(any(Long.class), any(ProductUpdateRequest.class));
                        }
                    }

                    @Nested
                    @DisplayName("가격이 0원 미만일 경우")
                    class Context_with_negative_price {

                        @Test
                        @DisplayName("400 코드로 응답한다")
                        void it_responses_400() throws Exception {
                            ResultActions perform = 상품_수정_API_요청(
                                    찾을_수_있는_id,
                                    유저_1번_정상_토큰,
                                    상품_가격_비정상
                            );

                            perform.andExpect(status().isBadRequest());

                            verify(productService, never())
                                    .updateProduct(any(Long.class), any(ProductUpdateRequest.class));
                        }
                    }
                }

                @Nested
                @DisplayName("유효한 상품 정보가 주어지면")
                class Context_with_valid_product {

                    @BeforeEach
                    void setUp() {
                        given(productService.updateProduct(eq(찾을_수_있는_id), any(ProductUpdateRequestDto.class)))
                                .will(invocation -> {
                                    Long productId = invocation.getArgument(0);
                                    ProductUpdateRequestDto product = invocation.getArgument(1);

                                    return Product.builder()
                                            .id(productId)
                                            .name(product.getName())
                                            .maker(product.getMaker())
                                            .price(product.getPrice())
                                            .imageUrl(product.getImageUrl())
                                            .build();
                                });
                    }

                    @Test
                    @DisplayName("200 코드로 응답한다")
                    void it_responses_200() throws Exception {
                        ResultActions perform = 상품_수정_API_요청(
                                찾을_수_있는_id,
                                유저_1번_정상_토큰,
                                상품_2번
                        );

                        perform.andExpect(status().isOk());

                        verify(productService).updateProduct(eq(찾을_수_있는_id), any(ProductUpdateRequestDto.class));
                    }
                }
            }
        }
    }


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class delete_메서드는 {

        @Nested
        @DisplayName("유효하지 않은 인증 토큰이 주어지면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = 상품_삭제_API_요청(
                        상품_1번.아이디(),
                        유저_1번_값_비정상_토큰
                );

                perform.andExpect(status().isUnauthorized());

                verify(productService, never()).deleteProduct(any(Long.class));
            }
        }

        @Nested
        @DisplayName("인증 토큰이 없다면")
        class Context_with_not_exist_token {
            @Test
            @DisplayName("401 코드로 응답한다")
            void it_responses_401() throws Exception {
                ResultActions perform = mockMvc.perform(
                        delete(REQUEST_PRODUCT_URL + "/" + 상품_1번.아이디())
                );

                perform.andExpect(status().isUnauthorized());

                verify(productService, never()).deleteProduct(any(Long.class));
            }
        }

        @Nested
        @DisplayName("인증 토큰이 유효하고")
        class Context_with_valid_token {

            @Nested
            @DisplayName("찾을 수 없는 id가 주어지면")
            class Context_with_not_exist_id {
                private final Long 찾을_수_없는_id = ID_MAX.value();

                @BeforeEach
                void setUp() {
                    given(productService.deleteProduct(찾을_수_없는_id))
                            .willThrow(new ProductNotFoundException());
                }

                @Test
                @DisplayName("404 코드로 응답한다")
                void it_responses_404() throws Exception {
                    ResultActions perform = 상품_삭제_API_요청(
                            찾을_수_없는_id,
                            유저_1번_정상_토큰
                    );

                    perform.andExpect(status().isNotFound());

                    verify(productService).deleteProduct(찾을_수_없는_id);
                }
            }
        }
    }


    private ResultActions 상품_등록_API_요청(final AuthHeaderFixture authHeaderFixture,
                                       final ProductFixture productFixture) throws Exception {
        return mockMvc.perform(
                post(REQUEST_PRODUCT_URL)
                        .header(HttpHeaders.AUTHORIZATION, authHeaderFixture.인증_헤더값())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(productFixture.등록_요청_데이터_생성()))
        );
    }

    private ResultActions 상품_수정_API_요청(final Long productId,
                                       final AuthHeaderFixture authHeaderFixture,
                                       final ProductFixture productFixture) throws Exception {
        return mockMvc.perform(
                patch(REQUEST_PRODUCT_URL + "/" + productId)
                        .header(HttpHeaders.AUTHORIZATION, authHeaderFixture.인증_헤더값())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueAsString(productFixture.수정_요청_데이터_생성()))
        );
    }

    private ResultActions 상품_삭제_API_요청(final Long productId,
                                       final AuthHeaderFixture authHeaderFixture) throws Exception {
        return mockMvc.perform(
                delete(REQUEST_PRODUCT_URL + "/" + productId)
                        .header(HttpHeaders.AUTHORIZATION, authHeaderFixture.인증_헤더값())
        );
    }
}
