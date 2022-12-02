package com.codesoom.assignment.utils;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

public interface ApiDocumentUtil {
    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                // 문서 상의 기본 uri 값인 localhost:8080을 변경해줍니다.
                modifyUris()
                        .scheme("https")
                        .host("docs.api.com")
                        .removePort(),
                // 문서의 request의 json 포맷을 예쁘게 정렬합니다.
                prettyPrint());
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        // 문서의 response의 json 포맷을 예쁘게 정렬합니다.
        return preprocessResponse(prettyPrint());
    }
}
