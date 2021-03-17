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
    private String role;

    public Role(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public Role(String role) {
        this(null, role);
    }
}
