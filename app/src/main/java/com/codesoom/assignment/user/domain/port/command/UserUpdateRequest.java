package com.codesoom.assignment.user.domain.port.command;

import com.codesoom.assignment.user.repository.User;

public interface UserUpdateRequest {
    String getName();

    String getPassword();

    User toEntity();
}
