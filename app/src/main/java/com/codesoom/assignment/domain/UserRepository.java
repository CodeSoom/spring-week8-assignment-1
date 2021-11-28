package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * 사용자 조회, 저장을 담당한다.
 */
public interface UserRepository {
    /**
     * 사용자 정보를 저장한다.
     *
     * @param user 저장할 사용자 정보
     * @return 저장한 사용자 정보
     */
    User save(User user);

    /**
     * 사용자의 특정 이메일 사용여부를 조회한다.
     *
     * @param email 조회할 이메일
     * @return 이메일 사용여부
     */
    boolean existsByEmail(String email);

    /**
     * id를 사용하여 특정한 사용자 정보를 조회한다.
     *
     * @param id 조회할 사용자의 id
     * @return 조회 결과
     */
    Optional<User> findById(Long id);

    /**
     * 삭제되지 않은 사용자를 조회한다.
     *
     * @param id 조회할 사용자의 id
     * @return 조회 결과
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 이메일을 이용하여 사용자를 조회한다.
     *
     * @param email 조회할 사용자의 이메일
     * @return 조회 결과
     */
    Optional<User> findByEmail(String email);
}
