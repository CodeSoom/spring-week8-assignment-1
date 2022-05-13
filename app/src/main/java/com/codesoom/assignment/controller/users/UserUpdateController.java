package com.codesoom.assignment.controller.users;

import com.codesoom.assignment.application.users.UserUpdateService;
import com.codesoom.assignment.domain.users.UserResponseDto;
import com.codesoom.assignment.domain.users.UserSaveDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

/** 회원 수정 요청을 처리합니다. */
@UserController
public class UserUpdateController {

    private final UserUpdateService service;

    public UserUpdateController(UserUpdateService service) {
        this.service = service;
    }

    @PreAuthorize("#id == authentication.principal")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UserSaveDto userSaveDto) {
        return new UserResponseDto(service.updateUser(id, userSaveDto));
    }

}