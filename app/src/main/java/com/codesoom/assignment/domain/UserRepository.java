package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    /**
     * 유저를 저장하고 리턴한다.
     *
     * @param user 유저
     * @return 유저
     */
    User save(User user);

    /**
     * 이메일이 저장되어 있으면 true, 없으면 false 를 리턴합니다.
     *
     * @param email 이메일
     * @return 저장되어 있으면 true, 없으면 false
     */
    boolean existsByEmail(String email);
    boolean existsById(Long id);

    /**
     * 모든 유저를 삭제합니다.
     *
     * 테스트에서 사용합니다.
     */
    void deleteAll();

    /**
     * 이메일로 유저를 찾아 리턴합니다.
     *
     * @param email 이메일
     * @return 유저
     */
    Optional<User> findByEmail(String email);

    /**
     * 식별자로 유저를 찾아 리턴합니다.
     *
     * @param id 식별자
     * @return 유저
     */
    Optional<User> findById(Long id);

    List<User> findAll();
    void deleteById(Long id);
}
