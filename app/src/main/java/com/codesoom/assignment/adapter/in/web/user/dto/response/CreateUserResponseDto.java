package com.codesoom.assignment.adapter.in.web.user.dto.response;

import com.codesoom.assignment.user.repository.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CreateUserResponseDto {
    @JsonIgnore
    private final User user;

    public CreateUserResponseDto(final User user) {
        this.user = user;
    }

    public Long getId() {
        return this.user.getId();
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public String getName() {
        return this.user.getName();
    }
}
