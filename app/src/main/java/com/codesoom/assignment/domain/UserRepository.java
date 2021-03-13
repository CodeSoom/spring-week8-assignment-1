package com.codesoom.assignment.domain;

import java.util.Optional;

/**
 * 회원 정보 레포지토리
 */
public interface UserRepository {
    /**
     * 회원 정보를 저장
     *
     * @param user 저장할 회원정보
     * @return 저장된 회원 정보
     */
    User save(User user);

    /**
     * Email의 중복 검사
     *
     * @param email 중복 검사 할 Email
     * @return 중복 검사 결과
     */
    boolean existsByEmail(String email);

    /**
     * 회원 정보 검색 및 비활성화 여부 확인
     *
     * @param id 검색할 회원정보 식별자
     * @return 회원의 Optional 객체
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * Email을 통한 회원 정보 검색
     *
     * @param email 검색 할 Email
     * @return 회원의 Optional 객체
     */
    Optional<User> findByEmail(String email);
}
