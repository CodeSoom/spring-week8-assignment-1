package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * 유저 저장소.
 */
public interface UserRepository {

    /**
     * 유저를 저장하고 반환한다.
     *
     * @param user 유저
     * @return 저장된 유저
     */
    User save(User user);

    /**
     * 유저 이메일이 존재하는 확인한다.
     *
     * @param email 유저 이메일
     * @return 유저가 존재하면 true, 아니라면 false.
     */
    boolean existsByEmail(String email);

    /**
     * 유저 식별자로 유저를 찾고, 유저를 반환한다.
     *
     * @param id 유저 식별자
     * @return 유저
     */
    Optional<User> findById(Long id);

    /**
     * 삭제되지 않고, 유저 식별자로 찾을 수 있는 유저를 반환한다.
     *
     * @param id 유저 식별자
     * @return 유저
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 유저 이메일에 해당하는 유저를 반환한다.
     *
     * @param email 유저 이메일
     * @return 유저
     */
    Optional<User> findByEmail(String email);
}
