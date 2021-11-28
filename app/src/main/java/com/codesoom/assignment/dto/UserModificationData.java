package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 사용자 수정 요청에 쓰이는 클래스
 */
@Getter
@Builder
@AllArgsConstructor
public class UserModificationData {
    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private String password;
}
