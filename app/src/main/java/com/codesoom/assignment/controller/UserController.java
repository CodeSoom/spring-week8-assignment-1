package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.user.UserCommandService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.user.UserModificationDto;
import com.codesoom.assignment.dto.user.UserRegistrationDto;
import com.codesoom.assignment.dto.user.UserResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserCommandService userCommandService;

    public UserController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultDto create(@RequestBody @Valid UserRegistrationDto registrationDto) {
        User user = userCommandService.create(registrationDto);
        return resultDto(user);
    }

    @PatchMapping("{id}")
    UserResultDto update(
            @PathVariable Long id,
            @RequestBody @Valid UserModificationDto modificationDto
    ) {
        User user = userCommandService.save(id, modificationDto);
        return resultDto(user);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        userCommandService.deleteById(id);
    }


    private UserResultDto resultDto(User user) {
        return UserResultDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();

    }

}
