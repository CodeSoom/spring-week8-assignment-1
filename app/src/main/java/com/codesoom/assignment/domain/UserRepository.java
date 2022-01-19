package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * 사용자 저장, 조회하는 기능을 제공한다.
 */
public interface UserRepository {
    /**
     * 사용자를 저장소에 저장한다.
     *
     * @param user 저정할 사용자
     * @return 저장한 사용자
     */
    User save(User user);

    /**
     * 사용자의 이메일이 존재하는지 확인하고 있으면 true 없다면 false를 리턴한다.
     *
     * @param email 확인할 이메일
     * @return 존재하면 true 존재하지 않으면 false
     */
    boolean existsByEmail(String email);

    /**
     * id에 해당하는 사용자를 찾아 리턴한다.
     *
     * @param id 사용자 id
     * @return 찾은 사용자
     */
    Optional<User> findById(Long id);

    /**
     * id에 해당하고 삭제되지않은 사용자를 찾아 리턴한다.
     *
     * @param id 사용자 id
     * @return 삭제되지 않은 사용자
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 이메일에 해당하는 사용자를 찾아 리턴한다.
     *
     * @param email 사용자 이메일
     * @return 찾은 사용자
     */
    Optional<User> findByEmail(String email);
}
