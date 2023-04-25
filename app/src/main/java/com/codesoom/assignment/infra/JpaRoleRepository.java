package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * JPA Repository For Role
 *
 * @author sim
 */
public interface JpaRoleRepository
        extends RoleRepository, CrudRepository<Role, Long> {

    /**
     * 유저 ID에 대한 권한 정보들을 조회한다.
     *
     * @param userId - 유저 고유 식별 ID.
     * @return 권한 리스트
     */
    List<Role> findAllByUserId(Long userId);

    /**
     * 권한을 저장한다.
     * 
     * @param role - 권한 엔티티
     * @return 저장된 권한
     */
    Role save(Role role);
}
