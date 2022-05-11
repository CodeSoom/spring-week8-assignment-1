package com.codesoom.assignment.dto.user;


import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserModificationDto {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 4, max = 512)
    private String password;

    @Builder
    public UserModificationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
