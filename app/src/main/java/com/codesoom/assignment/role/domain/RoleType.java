package com.codesoom.assignment.role.domain;

public enum RoleType {
    USER("USER"), ADMIN("ADMIN");

    private final String roleName;

    RoleType(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
