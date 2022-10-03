package com.codesoom.assignment.application.role;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleRegisterService implements RoleRegisterInterface {

    private final RoleRepository roleRepository;

    public RoleRegisterService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
