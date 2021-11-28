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
 * 사용자와 관련된 HTTP 요청을 처리하는 클래스
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자를 등록 후 리턴하고, 201 상태코드를 응답한다.
     * @param registrationData 등록할 사용자의 정보
     * @return 등록한 사용자
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }

    /**
     * 사용자 정보를 수정 후 리턴하고, 200 상태코드를 응답한다.
     * @param id 수정할 사용자의 id
     * @param modificationData 사용자의 수정할 정보
     * @param authentication 권한 검증을 위한 사용자 id
     * @return 수정된 사용자의 정보
     * @throws AccessDeniedException 사용자가 권한이 없는 경우
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
     * 요청한 id의 사용자를 삭제합니다.
     * @param id 삭제할 사용자의 id
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * 사용자 도메인을 응답 전용 데이터로 변환하여 리턴합니다.
     * @param user 사용자 도메인
     * @return 사용자의 정보
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
