package com.codesoom.assignment.domain;

public enum Role {
    ADMIN, USER;

    /**
     * 역할이 유저인지 확인하고 결과를 리턴한다.
     *
     * @param role 역할
     * @return 유저이면 true, 아니면 fasle
     */
    public static boolean isUser(Role role) {
        return USER.equals(role);
    }
}
