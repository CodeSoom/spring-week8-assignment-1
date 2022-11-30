package com.codesoom.assignment.session.adapter.in.web.dto.response;

import lombok.Getter;

@Getter
public class SessionResponseDto {
    private final String accessToken;

    public SessionResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
