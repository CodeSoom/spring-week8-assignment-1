package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.security.UserAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 회원의 CRUD에 대한 http 요청을 처리합니다.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 시 회원 정보를 받아 등록 후 회원 정보를 응답합니다.
     *
     * @param registrationData 회원가입 하려는 회원 정보
     * @return 가입한 회원 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }

    /**
     * 회원 정보 수정 요청을 처리합니다.
     * 로그인이 되어 있고, USER 권한이 있다면 수정하려는 회원 정보를 받아 회원 정보를 수정하여
     * 수정된 회원 정보를 응답합니다.
     *
     * @param id 회원 id
     * @param modificationData 수정된 회원 정보
     * @param authentication 로그인 한 회원 인증 정보
     * @return 기존 회원의 정보가 수정된 회원 정보
     * @throws AccessDeniedException 잘못된 권한으로 수정 요청했을 경우
     */
    @PatchMapping("{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    UserResultData update(
            @PathVariable Long id,
            @RequestBody @Valid UserModificationData modificationData,
            UserAuthentication authentication
    ) throws AccessDeniedException {
        Long userId = authentication.getUserId();
        User user = userService.updateUser(id, modificationData, userId);
        return getUserResultData(user);
    }

    /**
     * 회원 삭제 요청을 처리합니다.
     * 로그인이 되어 있고, ADMIN 권한이 있어야 합니다.
     *
     * @param id 삭제하려는 회원 id
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private UserResultData getUserResultData(User user) {
        if (user == null) {
            return null;
        }

        return UserResultData.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
