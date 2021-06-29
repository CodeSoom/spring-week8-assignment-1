package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 회원정보 갱신 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class UserModificationData {

    /**
     * 변경할 회원의 이름
     */
    @NotBlank
    @Mapping("name")
    private String name;

    /**
     * 변경할 회원의 비밀번호
     */
    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private String password;
}
