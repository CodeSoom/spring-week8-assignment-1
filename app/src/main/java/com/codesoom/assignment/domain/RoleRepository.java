package com.codesoom.assignment.domain;

import java.util.List;

/**
 * 유저 권한 저장소.
 */
public interface RoleRepository {
    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
