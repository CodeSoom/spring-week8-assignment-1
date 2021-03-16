package com.codesoom.assignment.user.domain;

import java.util.List;

/**
 * 권한 정보 저장소.
 */
public interface RoleRepository {
    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
