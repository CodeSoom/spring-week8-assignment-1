package com.codesoom.assignment.application.users;

import com.codesoom.assignment.domain.users.User;


/**
 * 회원 생성 요청.
 */
public interface UserSaveRequest {

    String getName();

    String getEmail();

    String getPassword();

    default User user() {
        return User.of(getName(), getEmail());
    }

}
