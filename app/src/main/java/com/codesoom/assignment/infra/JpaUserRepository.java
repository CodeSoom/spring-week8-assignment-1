package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaUserRepository
        extends UserRepository, CrudRepository<User, Long> {
    /**
     * 데이터베이스에 요청된 user를 저장한다. 
     * @param user 저장할 user.
     * @return 저장된 user.
     */
    User save(User user);

    /**
     * 요청된 email이 존재하는지 확인한다. 
     * @param email 확인 대상 email.
     * @return 존재하면 true를, 존재하지 않으면 false를 반환한다. 
     */
    boolean existsByEmail(String email);

    /**
     * 요청된 식별자와 일치하는 user를 반환한다. 
     * @param id user 식별자
     * @return 식별자와 일치하는 user.
     */
    Optional<User> findById(Long id);

    /**
     * 요청된 식별자와 일치하는 user를 찾아서 삭제되었는지 확인한다. 
     * @param id user 식별자. 
     * @return 식별자와 일치하는 user.
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 요청된 이메일과 일치하는 user를 반환한다. 
     * @param email 요청된 이메일. 
     * @return 요청된 이메일과 일치하는 user
     */
    Optional<User> findByEmail(String email);
}
