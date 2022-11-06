package com.codesoom.assignment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public Role(Long userId, RoleName roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }

    public Role(RoleName roleName) {
        this(null, roleName);
    }
}
