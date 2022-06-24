package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * User 저장
 */
public interface UserRepository {
    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

    Optional<User> findByEmail(String email);
}
