package com.codesoom.assignment.application.role;

import com.codesoom.assignment.domain.Role;

public interface RoleRegisterInterface {
    /**
     * 권한정보를 저장한다.
     *
     * @param role 저장할 권한
     * @return 저장된 권한
     */
    Role save(Role role);
}
