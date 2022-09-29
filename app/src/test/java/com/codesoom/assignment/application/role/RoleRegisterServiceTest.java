package com.codesoom.assignment.application.role;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class RoleRegisterServiceTest {

    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private RoleRegisterService roleRegisterService;

    @BeforeEach
    void setUp() {
        roleRegisterService = new RoleRegisterService(roleRepository);
    }

    @Nested
    @DisplayName("save()")
    class Describe_Save{

        private final Role ADMIN_ROLE = new Role(1L , "ADMIN");

        @BeforeEach
        void setUp() {
            given(roleRepository.save(any(Role.class))).willReturn(ADMIN_ROLE);
        }

        @Test
        @DisplayName("권한 정보를 저장한다.")
        void It_RoleSave(){
            final Role savedRole = roleRegisterService.save(ADMIN_ROLE);
            assertThat(savedRole.getName()).isEqualTo("ADMIN");
        }
    }
}
