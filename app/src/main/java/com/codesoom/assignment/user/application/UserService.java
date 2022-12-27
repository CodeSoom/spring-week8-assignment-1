package com.codesoom.assignment.user.application;

import com.codesoom.assignment.role.application.port.out.RoleRepository;
import com.codesoom.assignment.role.domain.Role;
import com.codesoom.assignment.user.application.exception.UserEmailDuplicationException;
import com.codesoom.assignment.user.application.exception.UserNotFoundException;
import com.codesoom.assignment.user.application.port.command.UserCreateRequest;
import com.codesoom.assignment.user.application.port.command.UserUpdateRequest;
import com.codesoom.assignment.user.application.port.in.UserUseCase;
import com.codesoom.assignment.user.application.port.out.UserRepository;
import com.codesoom.assignment.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codesoom.assignment.role.domain.RoleType.USER;


@Service
@Transactional
public class UserService implements UserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository,
                       final RoleRepository roleRepository,
                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(final UserCreateRequest userCreateRequest) {
        if (isAlreadyExistEmail(userCreateRequest)) {
            throw new UserEmailDuplicationException();
        }

        User user = userRepository.save(
                userCreateRequest.toEntity(passwordEncoder)
        );

        roleRepository.save(Role.builder()
                .userId(user.getId())
                .roleName(USER.getRoleName())
                .build()
        );

        return user;
    }

    public User updateUser(final Long id,
                           final UserUpdateRequest userUpdateRequest) {
        User user = findUser(id);

        user.update(userUpdateRequest.toEntity(passwordEncoder));

        return user;
    }

    public User deleteUser(final Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }


    private User findUser(final Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(UserNotFoundException::new);
    }

    private boolean isAlreadyExistEmail(final UserCreateRequest userCreateRequest) {
        return userRepository.existsByEmail(userCreateRequest.getEmail());
    }
}
