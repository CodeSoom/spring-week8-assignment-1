package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.user.UserDeleteInterface;
import com.codesoom.assignment.application.user.UserFindInterface;
import com.codesoom.assignment.application.user.UserFindService;
import com.codesoom.assignment.application.user.UserRegisterInterface;
import com.codesoom.assignment.application.user.UserUpdateInterface;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.error.UserEmailDuplicationException;
import com.codesoom.assignment.error.UserNotFoundException;
import com.codesoom.assignment.security.UserAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRegisterInterface userRegisterService;
    private final UserUpdateInterface userUpdateService;
    private final UserDeleteInterface userDeleteService;

    public UserController(UserRegisterInterface userRegisterService,
                          UserUpdateInterface userUpdateService,
                          UserDeleteInterface userDeleteService) {
        this.userRegisterService = userRegisterService;
        this.userUpdateService = userUpdateService;
        this.userDeleteService = userDeleteService;
    }

    /**
     * 사용자를 등록한다.
     *
     * @param registrationData 등록할 유저의 정보
     * @throws UserEmailDuplicationException 사용자의 Email이 중복되었을 경우
     * @return 저장된 유저의 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        return UserResultData.of(userRegisterService.registerUser(registrationData));
    }

    /**
     * 사용자를 수정한다.
     *
     * @param id 수정할 사용자의 식별자
     * @param modificationData 수정할 사용자의 정보
     * @throws AccessDeniedException 사용자 자원의 식별자와 수정자의 식별자가 다를 경우
     * @throws UserNotFoundException 식별자에 해당하는 사용자가 존재하지 않을 경우
     * @return 수정된 사용자
     */
    @PatchMapping("{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    UserResultData update(
            @PathVariable Long id,
            @RequestBody @Valid UserModificationData modificationData,
            UserAuthentication authentication
    ) throws AccessDeniedException {
        Long userId = authentication.getUserId();
        User user = userUpdateService.updateUser(id, modificationData, userId);
        return UserResultData.of(user);
    }

    /**
     * 사용자를 삭제한다.
     *
     * @param id 삭제할 사용자의 식별자
     * @throws UserNotFoundException 식별자에 해당하는 사용자가 존재하지 않을 경우
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    void destroy(@PathVariable Long id) {
        userDeleteService.deleteUser(id);
    }
}
