package com.codesoom.assignment.domain;

import java.util.List;

/**
 * 사용자 권한을 저장하고 찾는다.
 */
public interface RoleRepository {
    /**
     * 주어진 id에 해당하는 권한 목록을 반환한다.
     *
     * @param userId 사용자 id
     * @return 권한 목록
     */
    List<Role> findAllByUserId(Long userId);

    /**
     * 사용자 권한을 저장하고 반환한다.
     *
     * @param role 권한 정보
     * @return 저장한 권한
     */
    Role save(Role role);
}
