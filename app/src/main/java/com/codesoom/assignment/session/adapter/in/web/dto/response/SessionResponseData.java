package com.codesoom.assignment.session.adapter.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SessionResponseData {
    private String accessToken;
}
