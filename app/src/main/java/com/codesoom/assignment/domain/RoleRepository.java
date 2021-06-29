package com.codesoom.assignment.domain;

import java.util.List;

/**
 * 권한의 레포지토리
 */
public interface RoleRepository {

    /**
     * 권한 목록을 반환
     *
     * @param userId 회원의 고유 식별자
     * @return 지정된 사용자의 권한 목록
     */
    List<Role> findAllByUserId(Long userId);

    /**
     * 권한 정보를 저장
     *
     * @param role 저장할 권한 정보
     * @return 저장된 권한 정보
     */
    Role save(Role role);
}
