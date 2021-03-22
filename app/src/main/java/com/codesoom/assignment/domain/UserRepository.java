package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * 회원 저장소.
 */
public interface UserRepository {
    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

    Optional<User> findByEmail(String email);
}
