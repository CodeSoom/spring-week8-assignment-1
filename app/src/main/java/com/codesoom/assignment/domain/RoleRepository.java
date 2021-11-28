package com.codesoom.assignment.domain;

import java.util.List;

/**
 * 사용자 권한을 저장, 반환하는 클래스
 */
public interface RoleRepository {

    /**
     * 사용자 권한에 해당하는 권한 목록을 반환합니다.
     * @param userId 사용자의 식별자
     * @return 권한 목록
     */
    List<Role> findAllByUserId(Long userId);

    /**
     * 사용자 권한을 저장하고 반환합니다.
     * @param role 권한 정보
     * @return 저장한 권한
     */
    Role save(Role role);
}
