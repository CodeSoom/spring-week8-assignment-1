package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.user.UserRegistrationDto;

public interface UserCreator {
    User create(UserRegistrationDto registrationDto);
}
