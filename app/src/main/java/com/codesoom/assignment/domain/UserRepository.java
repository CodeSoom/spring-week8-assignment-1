package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * 사용자 저장소
 */
public interface UserRepository {
    Optional<User> findById(Long id);

    User save(User user);

    void delete(User user);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
