package com.codesoom.assignment.application.users;

import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.exceptions.UserNotFoundException;

/**
 * 회원 정보 삭제를 담당합니다.
 */
public interface UserDeleteService {

    /**
     * 식별자에 해당하는 회원의 삭제 상태를 true로 변경합니다.
     *
     * @param id 회원 식별자
     * @throws UserNotFoundException 식별자로 회원을 찾지 못한 경우
     */
    User deleteUser(Long id);

}
