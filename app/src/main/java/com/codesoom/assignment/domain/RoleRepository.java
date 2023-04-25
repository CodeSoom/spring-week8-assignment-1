package com.codesoom.assignment.domain;

import java.util.List;

public interface RoleRepository {

    /**
     * userId에 대한 모든 권한을 조회한다.
     *
     * @param userId - 유저 고유 식별 ID.
     * @return 권한 엔티티 리스트.
     */
    List<Role> findAllByUserId(Long userId);

    /**
     * userId에 대한 권한을 저장한다.
     * 
     * @param role - 권한 엔티티
     * @return 저장된 권한 엔티티
     */
    Role save(Role role);
}
