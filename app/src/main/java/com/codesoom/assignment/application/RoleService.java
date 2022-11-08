package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Role 을 관리하는 서비스
 */
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
