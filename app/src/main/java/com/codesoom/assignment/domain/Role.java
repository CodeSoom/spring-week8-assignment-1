package com.codesoom.assignment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;

/**
 * 사용자 권한 정보.
 */
@Entity
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private Long userId;

    @Getter
    private String name;

    public Role(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public Role(String name) {
        this(null, name);
    }
}
