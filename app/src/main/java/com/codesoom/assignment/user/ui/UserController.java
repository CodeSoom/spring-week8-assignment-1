package com.codesoom.assignment.user.ui;

import com.codesoom.assignment.auth.domain.UserAuthentication;
import com.codesoom.assignment.user.application.UserService;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.dto.UserModificationData;
import com.codesoom.assignment.user.dto.UserRegistrationData;
import com.codesoom.assignment.user.dto.UserResultData;
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

import javax.validation.Valid;

/**
 * 사용자에 대한 요청을 처리한다.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 주어진 사용자를 저장하고, 해당 사용자를 리턴한다.
     * @param registrationData 사용자 등록 명세서
     * @return 저장된 사용자
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }

    /**
     * 주어진 식별자에 해당하는 사용자를 수정하고, 해당 사용자를 리턴한다.
     * @param id 사용자 식별자
     * @param modificationData 사용자 수정 명세서
     * @param authentication 사용자 인증 정보
     * @return 수정된 사용자
     * @throws AccessDeniedException
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
     * 주어진 식별자에 해당하는 사용자를 삭제하고, 삭제된 사용자 정보를 리턴한다.
     * @param id 사용자 식별자
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
