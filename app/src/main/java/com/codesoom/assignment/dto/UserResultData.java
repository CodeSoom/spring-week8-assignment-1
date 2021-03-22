package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 사용자 조회에 사용한다. */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class UserResultData {
    private Long id;

    private String name;

    private String email;

    private String password;

    private boolean deleted;

    @Builder
    public UserResultData(Long id, String name, String email, String password, boolean deleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.deleted = deleted;
    }

    /** 상품 엔티티를 데이터로 바꾼다. */
    public static UserResultData of(User user) {
        return UserResultData.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .deleted(user.isDeleted())
                .build();
    }
}
