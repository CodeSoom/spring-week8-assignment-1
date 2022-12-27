package com.codesoom.assignment.support;

import com.codesoom.assignment.role.domain.Role;

import static com.codesoom.assignment.role.domain.RoleType.ADMIN;
import static com.codesoom.assignment.role.domain.RoleType.USER;
import static com.codesoom.assignment.support.AuthHeaderFixture.관리자_1004번_정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_2번_정상_토큰;

public enum RoleFixture {
    유저_1번_권한(유저_1번_정상_토큰.아이디(), USER.getRoleName()),
    유저_2번_권한(유저_2번_정상_토큰.아이디(), USER.getRoleName()),
    관리자_1004번_권한(관리자_1004번_정상_토큰.아이디(), ADMIN.getRoleName()),
    ;

    private final Long userId;
    private final String roleName;

    RoleFixture(Long userId, String roleName) {
        this.userId = userId;
        this.roleName = roleName;
    }

    public Role 권한_데이터_생성() {
        return Role.builder()
                .userId(this.userId)
                .roleName(this.roleName)
                .build();
    }

    public Long 아이디() {
        return userId;
    }

    public String 권한_이름() {
        return roleName;
    }
}
