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
 * Controller For User
 *
 * @author sim
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * UserController에 대한 생성자 메서드
     *
     * @param userService
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 유저를 등록한다.
     * 
     * @param registrationData - 등록할 유저 정보 객체
     * @return 유저 등록 결과 객체
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }

    /**
     * 유저 정보를 수정한다.
     * 메서드 실행 전 인증 및 USER 권한에 대한 인가 여부를 체크한다. 실패 시 401 에러를 응답한다.
     *
     * @param id - 수정할 유저 고유 식별 ID.
     * @param modificationData - 수정할 유저 정보 객체.
     * @param authentication - 현재 로그인된 User정보가 들어있는 인증 객체.
     * @return 수정된 유저 정보 객체.
     * @throws AccessDeniedException - 로그인된 유저의 ID와 수정할 유저의 ID가 다를 경우 예외 발생.
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
     * 유저 정보를 삭제한다.
     * ap서드 실행 전 인증 및 ADMIN 권한에 대한 인가 여부를 체크한다. 실패 시 401 에러를 응답한다.
     * 
     * @param id - 삭제할 유저 고유 식별 ID
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
