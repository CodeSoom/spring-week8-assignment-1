package com.codesoom.assignment;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.UriModifyingOperationPreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

public interface ApiDocumentUtils {
    UriModifyingOperationPreprocessor uriModifying = modifyUris()
            .scheme("https")
            .host("docs.api.com")
            .removePort();

    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                uriModifying,
                prettyPrint());
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
                uriModifying,
                prettyPrint());
    }
}

