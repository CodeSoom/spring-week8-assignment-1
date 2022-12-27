package com.codesoom.assignment.adapter.out.persistence.user;

import com.codesoom.assignment.user.application.port.out.UserRepository;
import com.codesoom.assignment.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaUserRepository
        extends UserRepository, CrudRepository<User, Long> {
    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

    Optional<User> findByEmail(String email);
}
