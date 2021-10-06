package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * 사용자를 저장소에 저장, 조회, 삭제 기능을 제공하는 클래스
 */
public interface UserRepository {

    /**
     * 사용자를 저장소에 저장합니다.
     * @param user 저장할 사용자 도메인
     * @return 저장한 사용자
     */
    User save(User user);

    /**
     * 사용자의 이메일이 존재하는지 확인합니다.
     * @param email 확인할 사용자 이메일
     * @return 중복 - true / 중복X - false
     */
    boolean existsByEmail(String email);

    /**
     * 사용자를 조회해 리턴합니다.
     * @param id 조회할 사용자의 id
     * @return 조회한 사용자
     */
    Optional<User> findById(Long id);

    /**
     * 사용자를 조회해 삭제합니다.
     * @param id 삭제할 사용자의 id
     * @return 삭제된 사용자
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 사용자를 조회해 리턴합니다.
     * @param email 조회할 사용자의 이메일
     * @return 조회한 사용자
     */
    Optional<User> findByEmail(String email);
}
