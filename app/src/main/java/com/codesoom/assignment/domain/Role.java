package com.codesoom.assignment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 유저 권한 정보.
 */
@Entity
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    @Getter
    private String name;

    /**
     * 유저 식별자와 이름으로 role을 설정한다.
     *
     * @param userId 유저 식별자
     * @param name 유저 이름
     */
    public Role(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    /**
     * 유저 이름으로 role을 설정한다.
     *
     * @param name 유저 이름
     */
    public Role(String name) {
        this(null, name);
    }
}
