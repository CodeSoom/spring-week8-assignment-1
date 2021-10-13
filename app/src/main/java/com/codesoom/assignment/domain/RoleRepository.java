package com.codesoom.assignment.domain;

import java.util.List;

/**
 * 권한 정보를 관리한다.
 */
public interface RoleRepository {
    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
