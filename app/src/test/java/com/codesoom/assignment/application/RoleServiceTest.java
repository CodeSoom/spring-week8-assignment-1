package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class RoleServiceTest {
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        roleService = new RoleService(roleRepository);

        given(roleRepository.findAllByUserId(1L))
                .willReturn(Arrays.asList(new Role("USER")));
        given(roleRepository.findAllByUserId(1004L))
                .willReturn(Arrays.asList(new Role("USER"), new Role("ADMIN")));
    }

    @Test
    void roles() {
        assertThat(
                roleService.getRoles(1L).stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        ).isEqualTo(Arrays.asList("USER"));

        assertThat(
                roleService.getRoles(1004L).stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        ).isEqualTo(Arrays.asList("USER", "ADMIN"));
    }

}
