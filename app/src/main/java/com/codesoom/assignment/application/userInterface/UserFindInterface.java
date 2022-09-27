package com.codesoom.assignment.application.userInterface;

import com.codesoom.assignment.domain.User;

public interface UserFindInterface {
    /**
     * 사용자를 조회한다.
     *
     * @param id 조회할 사용자의 식별자
     * @return 식별자에 해당하는 사용자
     */
    User findUser(Long id);
}
