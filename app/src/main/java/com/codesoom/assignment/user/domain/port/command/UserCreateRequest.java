package com.codesoom.assignment.user.domain.port.command;

import com.codesoom.assignment.user.repository.User;

public interface UserCreateRequest {
    String getEmail();

    String getName();

    String getPassword();

    User toEntity();
}
