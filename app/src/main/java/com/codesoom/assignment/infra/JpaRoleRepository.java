package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * 권한 데이터에 접근하는 JPA 구현체 입니다.
 */
public interface JpaRoleRepository
    extends RoleRepository, CrudRepository<Role, Long> {

    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
