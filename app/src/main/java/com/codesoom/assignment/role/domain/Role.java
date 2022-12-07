package com.codesoom.assignment.role.domain;

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

    // TODO: User 2차 리팩토링 진행하면서 같이 리팩토링
    public Role(final Long userId, final String name) {
        this.userId = userId;
        this.name = name;
    }

    public Role(final String name) {
        this(null, name);
    }
}
