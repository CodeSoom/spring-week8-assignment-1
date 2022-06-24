package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * DB에서 회원 조회, 저장 작업을 하는 레포지토리
 */
public interface UserRepository {
    /**
     * 회원을 저장한 후 저장된 회원을 반환한다.
     * 
     * @param user 저장할 회원
     * @return 저장된 회원
     */
    User save(User user);

    /**
     * 이메일이 일치하는 회원이 있으면 true를 반환한다.
     * 
     * @param email 회원 이메일
     * @return 이메일이 일치하는 회원을 찾으면 true, 찾지 못했으면 false를 반환한다.
     */
    boolean existsByEmail(String email);

    /**
     * id가 일치하는 회원을 반환한다.
     *
     * @param id 회원의 id
     * @return 조회된 회원
     */
    Optional<User> findById(Long id);

    /**
     * id가 일치하고 deleted 속성이 false인 회원을 반환한다.
     * @param id 회원의 id
     * @return 조회된 회원
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 이메일이 일치하는 회원을 반환한다.
     * 
     * @param email 회원의 이메일
     * @return 조회된 회원
     */
    Optional<User> findByEmail(String email);
}
