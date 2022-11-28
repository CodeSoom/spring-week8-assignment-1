package com.codesoom.assignment.role.adapter.out.persistence;

import com.codesoom.assignment.role.domain.Role;
import com.codesoom.assignment.role.domain.RoleRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JpaRoleRepository
        extends RoleRepository, CrudRepository<Role, Long> {
    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
