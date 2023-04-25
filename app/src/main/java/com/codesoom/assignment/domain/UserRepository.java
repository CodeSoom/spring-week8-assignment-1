package com.codesoom.assignment.domain;

import java.util.Optional;

public interface UserRepository {

    /**
     * 유저를 저장한다.
     *
     * @param user - 저장할 유저 엔티티.
     * @return 저장된 유저 엔티티.
     */
    User save(User user);

    /**
     * 이메일 존재 여부를 확인한다.
     *
     * @param email - 존재 여부 확인할 이메일.
     * @return 확인 결과
     */
    boolean existsByEmail(String email);

    /**
     * Id에 대한 유저를 조회한다.
     * 
     * @param id - 유저 고유 식별 ID
     * @return 조회된 유저 Optional 객체
     */
    Optional<User> findById(Long id);

    /**
     * 삭제되지 않은 ID에 대한 유저를 조회한다.
     * 
     * @param id - 유저 고유 식별 ID
     * @return 조회된 유저 Optional 객체
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 이메일에 대한 유저를 조회한다.
     * 
     * @param email - 이메일
     * @return 해당 이메일을 가진 유저 Optional 객체
     */
    Optional<User> findByEmail(String email);
}
