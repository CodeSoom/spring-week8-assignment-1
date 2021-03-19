package com.codesoom.assignment.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 사용자 권한.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    /**
     * 권한 식별자.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 식별자.
     */
    private Long userId;

    /**
     * 권한명.
     */
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
