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
 * 유저 HTTP 요청 핸들러.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 전달받은 유저 정보로 새로운 유저를 생성하고 응답합니다.
     *
     * @param registrationData 유저 생성 정보
     * @return 생성된 유저
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }


    /**
     * 전달받은 id와 일치하는 유저를 주어진 유저 정보로 수정하고 응답합니다.
     *
     * @param id               수정 대상 유저 식별자
     * @param modificationData 유저 수정 정보
     * @param authentication   수정 요청 유저 정보
     * @return 수정된 유저
     * @throws AccessDeniedException 수정 권한이 없는 경우
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
     * 전달받은 id와 일치하는 유저를 삭제합니다.
     *
     * @param id 유저 식별자
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * 주어진 유저를 유저 응답 정보로 반환합니다.
     *
     * @param user 유저
     * @return 유저 응답 정보
     */
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
