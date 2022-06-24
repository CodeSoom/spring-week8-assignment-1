package com.codesoom.assignment.domain;

import java.util.List;

/**
 * User 권한을 저장하고 반환
 */
public interface RoleRepository {
    /**
     * 주어진 id 와 일치하는 User 권한 목록 반환
     * @param userId id
     * @return User 권한 목록
     */
    List<Role> findAllByUserId(Long userId);

    /**
     * User 권한을 저장하고 반환
     *
     * @param role User 권한
     * @return 저장된 User 권한
     */
    Role save(Role role);
}
