package com.codesoom.assignment.domain.users;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {

    List<Role> findAllByUserId(Long userId);

}
