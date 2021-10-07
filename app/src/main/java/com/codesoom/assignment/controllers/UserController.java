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
 * 회원 등록, 정보 변경, 삭제 HTTP 요청을 서비스 객체로 전달합니다.
 */

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 신규 회원 데이터를 서비스에 전달하여 등록된 회원 데이터를 리턴합니다.
     * @param registrationData 등록하려는 회원 데이터
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }

    /**
     * 주어진 아이디로 변경하려는 회원 데이터를 서비스에 전달하여 변경된 회원 데이터를 리턴합니다.
     * @param id 회원 아이디
     * @param modificationData 변경하려는 회원 데이터
     * @param authentication 사용자 인증
     * @throws AccessDeniedException 회원 인증이 실패했을 때
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
     * 주어진 아이디로 서비스에 삭제 요청을 전달합니다.
     * @param id 회원 아이디
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
