package com.codesoom.assignment.domain.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    protected Role() {
    }

    public Role(UserRole role) {
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }

}
