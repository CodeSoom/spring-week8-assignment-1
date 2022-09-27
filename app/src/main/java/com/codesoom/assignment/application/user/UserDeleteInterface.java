package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;

public interface UserDeleteInterface {
    /**
     * 사용자를 삭제한다.
     *
     * @param id 삭제할 사용자의 식별자
     * @return 삭제된 사용자
     */
    User deleteUser(Long id);
}
