package com.codesoom.assignment.user.domain;

import com.codesoom.assignment.role.repository.Role;
import com.codesoom.assignment.role.repository.RoleRepository;
import com.codesoom.assignment.user.domain.port.command.UserCreateRequest;
import com.codesoom.assignment.user.domain.port.command.UserUpdateRequest;
import com.codesoom.assignment.user.domain.port.in.UserUseCase;
import com.codesoom.assignment.user.exception.UserEmailDuplicationException;
import com.codesoom.assignment.user.exception.UserNotFoundException;
import com.codesoom.assignment.user.repository.User;
import com.codesoom.assignment.user.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
