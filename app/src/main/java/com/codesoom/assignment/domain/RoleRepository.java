package com.codesoom.assignment.domain;

import java.util.List;

/**
 * 권환 저장소.
 */
public interface RoleRepository {
    /**
     * 회원 권환 리스트를 조회한다.
     * @param userId
     * @return
     */
    List<Role> findAllByUserId(Long userId);

    /**
     * 회원 권한을 저장한다.
     * @param role
     * @return
     */
    Role save(Role role);
}
