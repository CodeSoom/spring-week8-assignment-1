package com.codesoom.assignment.controller.user;

import com.codesoom.assignment.application.user.UserDeleteInterface;
import com.codesoom.assignment.error.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserDeleteController {

    private final UserDeleteInterface userDeleteService;

    public UserDeleteController(UserDeleteInterface userDeleteService) {
        this.userDeleteService = userDeleteService;
    }

    /**
     * 사용자를 삭제한다.
     *
     * @param id 삭제할 사용자의 식별자
     * @throws UserNotFoundException 식별자에 해당하는 사용자가 존재하지 않을 경우
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    void destroy(@PathVariable Long id) {
        userDeleteService.deleteUser(id);
    }
}
