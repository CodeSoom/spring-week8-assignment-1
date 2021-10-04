package com.codesoom.assignment.domain;

import java.util.List;

/**
 * 유저 권한 저장소.
 */
public interface RoleRepository {

    /**
     * 유저 식별자에 해당하는 유저 권한 리스트를 반환한다.
     *
     * @param userId 유저 식별자
     * @return 유저 권한 리스트
     */
    List<Role> findAllByUserId(Long userId);

    /**
     * 유저 권한을 저장하고 반환한다.
     *
     * @param role 유저 권한
     * @return 유저 권한
     */
    Role save(Role role);
}
