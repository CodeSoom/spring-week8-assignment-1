package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.config.BeanIds;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProductControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(WebApplicationContext wac,
               RestDocumentationContextProvider restDocumentationContextProvider) throws ServletException {
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();

        delegatingFilterProxy.init(
                new MockFilterConfig(wac.getServletContext(), BeanIds.SPRING_SECURITY_FILTER_CHAIN)
        );

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilter(delegatingFilterProxy)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        given(productService.getProducts()).willReturn(List.of(product));

        given(productService.getProduct(1L)).willReturn(product);

        given(productService.getProduct(1000L))
                .willThrow(new ProductNotFoundException(1000L));

        given(productService.createProduct(any(ProductData.class)))
                .willReturn(product);

        given(productService.updateProduct(eq(1L), any(ProductData.class)))
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    ProductData productData = invocation.getArgument(1);
                    return Product.builder()
                            .id(id)
                            .name(productData.getName())
                            .maker(productData.getMaker())
                            .price(productData.getPrice())
                            .build();
                });

        given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                .willThrow(new ProductNotFoundException(1000L));

        given(productService.deleteProduct(1000L))
                .willThrow(new ProductNotFoundException(1000L));

        given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);

        given(authenticationService.parseToken(INVALID_TOKEN))
                .willThrow(new InvalidTokenException(INVALID_TOKEN));

        given(authenticationService.roles(1L))
                .willReturn(Arrays.asList(new Role("USER")));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(
                        get("/products")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("get-products"));
    }

    @Test
    void deatilWithExsitedProduct() throws Exception {
        mockMvc.perform(
                        get("/products/1")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("get-product"));
    }

    @Test
    void deatilWithNotExsitedProduct() throws Exception {
        mockMvc.perform(get("/products/1000"))
                .andExpect(status().isNotFound())
                .andDo(document("get-product-not-exsited"));
    }

    @Test
    void createWithValidAttributes() throws Exception {
        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")))
                .andDo(document("post-product"));

        verify(productService).createProduct(any(ProductData.class));
    }

    @Test
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"maker\":\"\"," +
                                        "\"price\":0}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("post-product-invalid"));
    }

    @Test
    void createWithoutAccessToken() throws Exception {
        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                .andExpect(status().isUnauthorized())
                .andDo(document("post-product-without-accessToken"));
    }

    @Test
    void createWithWrongAccessToken() throws Exception {
        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized())
                .andDo(document("post-product-with-wrong-accessToken"));
    }

    @Test
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(
                        patch("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐순이")))
                .andDo(document("patch-product"));

        verify(productService).updateProduct(eq(1L), any(ProductData.class));
    }

    @Test
    void updateWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                        patch("/products/1000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isNotFound())
                .andDo(document("patch-product-not-exist"));

        verify(productService).updateProduct(eq(1000L), any(ProductData.class));
    }

    @Test
    void updateWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                        patch("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"maker\":\"\"," +
                                        "\"price\":0}")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("patch-product-with-invalid"));
    }

    @Test
    void updateWithoutAccessToken() throws Exception {
        mockMvc.perform(
                        patch("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                .andExpect(status().isUnauthorized())
                .andDo(document("patch-product-without-accessToken"));
    }

    @Test
    void updateWithInvalidAccessToken() throws Exception {
        mockMvc.perform(
                        patch("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized())
                .andDo(document("patch-product-with-invalid-accessToken"));
    }

    @Test
    void destroyWithExistedProduct() throws Exception {
        mockMvc.perform(
                        delete("/products/1")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(document("delete-product"));

        verify(productService).deleteProduct(1L);
    }

    @Test
    void destroyWithNotExistedProduct() throws Exception {
        mockMvc.perform(
                        delete("/products/1000")
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                .andExpect(status().isNotFound())
                .andDo(document("delete-product-with-not-exist"));

        verify(productService).deleteProduct(1000L);
    }

    @Test
    void destroyWithoutAccessToken() throws Exception {
        mockMvc.perform(
                        patch("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                )
                .andExpect(status().isUnauthorized())
                .andDo(document("delete-product-without-accessToken"));
    }

    @Test
    void destroyWithInvalidAccessToken() throws Exception {
        mockMvc.perform(
                        patch("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
                                        "\"price\":5000}")
                                .header("Authorization", "Bearer " + INVALID_TOKEN)
                )
                .andExpect(status().isUnauthorized())
                .andDo(document("delete-product-with-invalid"));
    }
}
