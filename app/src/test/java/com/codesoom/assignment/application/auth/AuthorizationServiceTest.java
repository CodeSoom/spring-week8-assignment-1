package com.codesoom.assignment.application.auth;

import com.codesoom.assignment.application.ServiceTest;
import com.codesoom.assignment.domain.users.Role;
import com.codesoom.assignment.domain.users.RoleRepository;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class AuthorizationServiceTest extends ServiceTest {

    private static final String EMAIL = "test@codesoom.com";
    private static final String PASSWORD = "pAsSwOrD!2#";

    private UserAuthorizationService service;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        this.service = new UserAuthorizationService(roleRepository, jwtUtil);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    User saveUser() {
        User user = User.of("김철수", EMAIL);
        user.changePassword(PASSWORD, passwordEncoder);

        return repository.save(user);
    }

    @DisplayName("roles 메서드는")
    @Nested
    class Describe_roles {

        @DisplayName("찾을 수 있는 회원의 id가 주어지면")
        @Nested
        class Context_with_exist_user {

            private Long EXIST_USER_ID;

            @BeforeEach
            void setup() {
                this.EXIST_USER_ID = saveUser().getId();
            }

            @DisplayName("회원이 가진 권한을 반환한다.")
            @Test
            void it_returns_users_roles() {
                List<Role> roles = service.roles(EXIST_USER_ID);

                assertThat(roles).isNotEmpty();
            }

        }

    }
}
