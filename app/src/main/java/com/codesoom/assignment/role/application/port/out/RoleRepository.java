package com.codesoom.assignment.role.application.port.out;

import com.codesoom.assignment.role.domain.Role;

import java.util.List;

public interface RoleRepository {
    List<Role> findAllByUserId(final Long userId);

    Role save(final Role role);
}
