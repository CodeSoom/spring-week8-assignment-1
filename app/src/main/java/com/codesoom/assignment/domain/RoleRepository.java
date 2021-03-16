package com.codesoom.assignment.domain;

import java.util.List;

/** 계정을 처리하는 저장소이다. */
public interface RoleRepository {
    /** 주어진 이메일로 계정을 조회한다. */
    List<Role> findAllByEmail(String email);

    /** 주어진 게정을 저장한다. */
    Role save(Role role);
}
