package com.codesoom.assignment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
     * Role에 대한 생성자 메서드
     *
     * @param userId - 유저 고유 식별 ID
     * @param name - 인가할 권한 이름
     */
    public Role(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    /**
     * Role에 대한 생성자 메서드
     * 
     * @param name - 인가할 권한 이름
     */
    public Role(String name) {
        this(null, name);
    }
}
