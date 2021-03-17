package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * 유저 저장소.
 */
public interface UserRepository {
    /**
     * 전달받은 유저를 저장하고 반환합니다.
     *
     * @param user 유저
     * @return 저장된 유저
     */
    User save(User user);

    /**
     * 전달받은 email과 일치하는 유저의 존재 여부를 반환합니다.
     *
     * @param email 유저 email
     * @return 유저 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * 전달받은 식별자와 일치하는 유저를 반환합니다.
     *
     * @param id 유저 식별자
     * @return 유저
     */
    Optional<User> findById(Long id);

    /**
     * 삭제되지 않은 유저중 전달받은 식별자와 일치하는 유저를 반환합니다.
     *
     * @param id 유저 식별자
     * @return 유저
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 전달받은 email과 일치하는 유저를 반환합니다.
     *
     * @param email 유저 email
     * @return 유저
     */
    Optional<User> findByEmail(String email);
}
