package com.codesoom.assignment.domain;

import java.util.List;

/**
 * 사용자 권한 생성 및 조회를 담당한다.
 */
public interface RoleRepository {
    /**
     * 사용자 권한 목록을 조회한다.
     *
     * @param userId 조회할 사용자의 id
     * @return 사용자 권한 목록
     */
    List<Role> findAllByUserId(Long userId);

    /**
     * 사용자 권한을 저장한다.
     *
     * @param role 저장할 사용자 권한
     * @return 저장한 사용자 권한
     */
    Role save(Role role);
}
