package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/** 사용자를 처리하는 저장소이다. */
public interface UserRepository {
    /** 모든 사용자 목록을 리턴한다. */
    List<User> findAll();

    /** 주어진 이메일에 해당하는 사용자가 존재하는 검사한다. */
    boolean existsByEmail(String email);

    /** 주어진 이메일에 해당하는 사용자를 조회한다.  */
    Optional<User> findByEmail(String email);

    /** 주어진 사용자를 저장하고 해당 객체를 리턴한다. */
    User save(User user);

    /** 주어진 사용자를 삭제한다. */
    void delete(User user);

    /** 주어진 아이디에 대하여 저장되어 있고 삭제되어지지 않은 사용자를 조회한다. */
    Optional<User> findByIdAndDeletedIsFalse(Long id);
}
