package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.security.UserAuthentication;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 관련 HTTP 요청 처리 담당.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원 생성 데이터로 회원을 생성하고 리턴합니다.
     *
     * @param registrationData 회원 생성 데이터
     * @return 생성된 회원
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }

    /**
     * 식별자로 회원 정보를 찾아 수정하고, 리턴합니다.
     * <p>
     * 유저 권한을 가진 인증된 유저만 이 요청을 수행할 수 있습니다.
     *
     * @param id               식별자
     * @param modificationData 수정할 회원 정보
     * @param authentication   회원 인증 정보
     * @return 수정된 회원 정보
     * @throws AccessDeniedException 다른 사람의 정보를 수정하려는 경우
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
     * 식별자로 회원을 찾아 삭제합니다.
     * <p>
     * 어드민 권한을 가진 인증된 유저만 이 요청을 수행할 수 있습니다.
     *
     * @param id 식별자
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
