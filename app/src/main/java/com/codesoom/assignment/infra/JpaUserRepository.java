package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * JPA로 회원 데이터에 접근하는 역할을 합니다.
 */
public interface JpaUserRepository
    extends UserRepository, CrudRepository<User, Long> {

    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

    Optional<User> findByEmail(String email);
}
