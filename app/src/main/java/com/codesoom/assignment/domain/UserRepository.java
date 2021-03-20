package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * 유저 저장소
 */
public interface UserRepository {
    /**
     * 유저를 저장한다.
     * @param user 유저
     * @return 생성된 유저
     */
    User save(User user);

    /**
     * 존재하는 이메일인지 확인한다.
     * @param email 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * 아이디로 회원 정보를 조회한다.
     * @param id
     * @return
     */
    Optional<User> findById(Long id);

    /**
     * 삭제되지 않은 유저를 반환한다.
     * @param id
     * @return
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 이메일로 유저를 찾는다.
     * @param email
     * @return
     */
    Optional<User> findByEmail(String email);
}
