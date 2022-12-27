package com.codesoom.assignment.presentation;

import com.codesoom.assignment.MockMvcCharacterEncodingCustomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * MockMvc을 설정하여 제공하는 객체입니다. <br>
 * 해당 객체를 상속 받을 경우 `@WebMvcTest` 혹은 `@SpringBootTest`를 통해 WebApplicationContext를 제공해야됩니다.
 */
@Import(MockMvcCharacterEncodingCustomizer.class)
@ExtendWith({RestDocumentationExtension.class})
public class RestDocsMockMvcProvider {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @BeforeEach
    void setUpMockMvc(final RestDocumentationContextProvider restDcosContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDcosContextProvider))
                // Spring Security 필터 체인을 주입합니다.
                .addFilter(springSecurityFilterChain)
                // 테스트의 결과 값을 UTF-8로 설정해줍니다.
                // (Spring Boot 2.2부터는 Spring에서 charset을 사용하지 않고 기본 인코딩 문자도 더 이상 UTF-8이 아닙니다.)
                // Ref: https://github.com/spring-projects/spring-framework/issues/22788
                .alwaysDo(result -> result.getResponse().setCharacterEncoding(StandardCharsets.UTF_8.name()))
                .alwaysDo(print())
                .build();
    }
}
