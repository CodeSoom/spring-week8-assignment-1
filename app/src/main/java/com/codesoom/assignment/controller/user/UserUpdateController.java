package com.codesoom.assignment.controller.user;

import com.codesoom.assignment.application.user.UserUpdateInterface;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.error.UserNotFoundException;
import com.codesoom.assignment.security.UserAuthentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserUpdateController {

    private final UserUpdateInterface userUpdateService;

    public UserUpdateController(UserUpdateInterface userUpdateService) {
        this.userUpdateService = userUpdateService;
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
        User user = userUpdateService.updateUser(id, modificationData.addUserId(userId));
        return UserResultData.of(user);
    }
}
