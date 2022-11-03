package com.codesoom.assignment.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;

@TestConfiguration
public class RestDocsConfig {

    @Bean
    public RestDocumentationResultHandler write() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}", // 스니펫이 생성되는 경로를 테스트를 수행하는 클래스/메서드명으로 정합니다.
                Preprocessors.preprocessRequest(
                        Preprocessors.prettyPrint(),  // 요청을 예쁘게 출력합니다.
                        modifyUris()
                                .host("api.toy-shop.com") // Host URL을 변경합니다.
                                .removePort() // Host에서 포트 번호를 숨깁니다.
                        ),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()) // 응답을 예쁘게 출력합니다.
        );
    }
}
