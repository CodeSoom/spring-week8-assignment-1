package com.codesoom.assignment.user.domain.port.in;

import com.codesoom.assignment.user.domain.port.command.UserCreateRequest;
import com.codesoom.assignment.user.domain.port.command.UserUpdateRequest;
import com.codesoom.assignment.user.repository.User;

public interface UserUseCase {
    /**
     * 회원을 등록하고 리턴합니다.
     *
     * @param createUserRequest 등록할 회원 정보
     * @return 등록한 회원 리턴
     */
    User createUser(final UserCreateRequest createUserRequest);

    /**
     * 회원을 수정하고 리턴합니다.
     *
     * @param id                회원 고유 id
     * @param updateUserRequest 수정할 회원 정보
     * @param userId            인증 토큰의 회원 id
     * @return 수정한 회원 리턴
     */
    User updateUser(final Long id, final UserUpdateRequest updateUserRequest, final Long userId);

    /**
     * 회원을 삭제하고 리턴합니다.
     *
     * @param id 회원 고유 id
     * @return 삭제한 회원 id 리턴
     */
    User deleteUser(final Long id);
}
