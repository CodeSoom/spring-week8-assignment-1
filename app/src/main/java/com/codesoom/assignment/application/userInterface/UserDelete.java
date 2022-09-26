package com.codesoom.assignment.application.userInterface;

import com.codesoom.assignment.domain.User;

public interface UserDelete {
    /**
     * 사용자를 삭제한다.
     *
     * @param id 삭제할 사용자의 식별자
     * @return 삭제된 사용자
     */
    User deleteUser(Long id);
}
