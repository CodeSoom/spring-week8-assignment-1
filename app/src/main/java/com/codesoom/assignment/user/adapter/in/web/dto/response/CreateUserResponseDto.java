package com.codesoom.assignment.user.adapter.in.web.dto.response;

import com.codesoom.assignment.user.domain.User;
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
