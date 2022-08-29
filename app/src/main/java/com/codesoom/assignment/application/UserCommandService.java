package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserRegisterRequest;

public interface UserCommandService {
    /**
     * 유저 등록 정보를 받아 유저를 생성하고 유저를 리턴합니다.
     *
     * @param request 유저 등록 요청
     * @return 유저
     */
    User register(UserRegisterRequest request);

    /**
     * 유저 식별자를 받아 유저를 삭제합니다.
     *
     * @param id Long 식별자
     */
    void delete(Long id);
}
