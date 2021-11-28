package com.codesoom.assignment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 권한.
 */
@Entity
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 회원 식별자.
     */
    private Long userId;

    /**
     * 권한 이름.
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
