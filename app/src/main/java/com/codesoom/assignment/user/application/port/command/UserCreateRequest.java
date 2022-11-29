package com.codesoom.assignment.user.application.port.command;

import com.codesoom.assignment.user.domain.User;

public interface UserCreateRequest {
    String getEmail();

    String getName();

    String getPassword();

    User toEntity();
}
