package com.codesoom.assignment.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository {
    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
