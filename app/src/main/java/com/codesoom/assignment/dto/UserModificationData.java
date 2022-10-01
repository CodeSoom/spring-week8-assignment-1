package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
public class UserModificationData {
    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private String password;

    private Long userId;

    @Builder
    private UserModificationData(String name, String password, Long userId) {
        this.name = name;
        this.password = password;
        this.userId = userId;
    }

    public UserModificationData addUserId(Long userId){
        return UserModificationData.builder()
                                    .name(this.name)
                                    .password(this.password)
                                    .userId(userId)
                                    .build();
    }

    public boolean isDifferentUser(Long userId){
        return !Objects.equals(this.userId, userId);
    }
}
