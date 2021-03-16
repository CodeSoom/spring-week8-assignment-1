package com.codesoom.assignment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 로그인 결과에 사용한다. */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class SessionResultData {
    private String accessToken;

    @Builder
    public SessionResultData(String accessToken) {
        this.accessToken = accessToken;
    }

    /** 토큰 문자열을 데이터로 바꾼다. */
    public static SessionResultData of(String accessToken) {
        return SessionResultData.builder()
                .accessToken(accessToken)
                .build();
    }
}
