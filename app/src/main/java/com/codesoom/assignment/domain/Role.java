package com.codesoom.assignment.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** 계정 정보를 다룬다. */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Role {
    /** 계정 식별자 */
    @Id
    @GeneratedValue
    private Long id;

    /** 계정 사용자 이메일 */
    private String email;

    /** 계정 사용자 이름 */
    private String name;

    @Builder
    public Role(String name) {
        this(null, name);
    }

    @Builder
    public Role(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
