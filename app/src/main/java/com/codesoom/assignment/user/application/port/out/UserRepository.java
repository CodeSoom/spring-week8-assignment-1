package com.codesoom.assignment.user.application.port.out;

import com.codesoom.assignment.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    User save(final User user);

    boolean existsByEmail(final String email);

    Optional<User> findById(final Long id);

    Optional<User> findByIdAndDeletedIsFalse(final Long id);

    Optional<User> findByEmail(final String email);
}
