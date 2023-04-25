package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * JPA Repository For User
 *
 * @author sim
 */
public interface JpaUserRepository
        extends UserRepository, CrudRepository<User, Long> {

    /**
     * 유저를 저장한다.
     * 
     * @param user - 저장할 유저 엔티티.
     * @return 저장된 유저정보
     */
    User save(User user);

    /**
     * 이메일 존재 여부를 확인한다.
     *
     * @param email - 존재 여부 확인할 이메일.
     * @return 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * ID에 대한 유저정보 조회
     * 
     * @param id - 유저 고유 식별 ID
     * @return 유저 정보에 대한 Optional 객체
     */
    Optional<User> findById(Long id);

    /**
     * ID에 대한 삭제되지 않은 유저정보 조회
     * 
     * @param id - 유저 고유 식별 ID
     * @return 유저 정보에 대한 Optional 객체
     */
    Optional<User> findByIdAndDeletedIsFalse(Long id);

    /**
     * 이메일에 대한 유저정보 조회
     * 
     * @param email - 이메일
     * @return 유저 정보에 대한 Optional 객체
     */
    Optional<User> findByEmail(String email);
}
