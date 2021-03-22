package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/** 계정 저장소를 구현한다. */
public interface JpaRoleRepository
        extends RoleRepository, CrudRepository<Role, String> {
    List<Role> findAllByEmail(String email);

    Role save(Role role);
}
