package com.codesoom.assignment;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;

import java.nio.charset.StandardCharsets;

/**
 * 전역적으로 MockMvc의 동작을 설정합니다. <br>
 * `@AutoconfigureMockMvc`를 통해 MockMvc를 주입받는 상황에서만 적용됩니다.
 */
@Component
public class MockMvcCharacterEncodingCustomizer implements MockMvcBuilderCustomizer {
    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {
        builder.alwaysDo(result ->
                // Spring Boot 2.2부터는 Spring에서 charset을 사용하지 않으며, 기본 인코딩 문자도 더 이상 UTF-8이 아닙니다.
                // 따라서 테스트의 결과 값을 UTF-8로 설정해줍니다.
                // Ref: https://github.com/spring-projects/spring-framework/issues/22788
                result.getResponse().setCharacterEncoding(StandardCharsets.UTF_8.name())
        );
    }
}
