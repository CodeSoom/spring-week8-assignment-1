package com.codesoom.assignment.adapter.out.persistence.role;

import com.codesoom.assignment.role.repository.Role;
import com.codesoom.assignment.role.repository.RoleRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JpaRoleRepository
        extends RoleRepository, CrudRepository<Role, Long> {
    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
