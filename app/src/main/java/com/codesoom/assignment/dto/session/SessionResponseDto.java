package com.codesoom.assignment.dto.session;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionResponseDto {
    private String accessToken;

    @Builder
    public SessionResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
