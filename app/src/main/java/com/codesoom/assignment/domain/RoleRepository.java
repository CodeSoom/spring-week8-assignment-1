package com.codesoom.assignment.domain;

import java.util.List;

/**
 * 권한 데이터 접근에 대한 처리를 담당합니다.
 */
public interface RoleRepository {

    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
