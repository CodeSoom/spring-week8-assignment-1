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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        String email = userCreateRequest.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException();
        }

        User user = userRepository.save(userCreateRequest.toEntity());

        // TODO: 미리 비밀번호 encoding 후 save 진행
        user.changePassword(userCreateRequest.getPassword(), passwordEncoder);

        roleRepository.save(new Role(user.getId(), "USER"));

        return user;
    }

    public User updateUser(final Long id,
                           final UserUpdateRequest userUpdateRequest,
                           final Long userId) throws AccessDeniedException {
        // TODO: 인가 로직 AOP로 분리
        if (!id.equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        User user = findUser(id);

        user.update(userUpdateRequest.toEntity(), passwordEncoder);

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
}
