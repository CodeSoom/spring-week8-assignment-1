package com.codesoom.assignment.adapter.in.web.session.dto.response;

import lombok.Getter;

@Getter
public class SessionResponseDto {
    private final String accessToken;

    public SessionResponseDto(final String accessToken) {
        this.accessToken = accessToken;
    }
}
