package com.codesoom.assignment.user.ui;

import com.codesoom.assignment.user.domain.Role;
import com.codesoom.assignment.user.domain.RoleRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JpaRoleRepository
        extends RoleRepository, CrudRepository<Role, Long> {
    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
