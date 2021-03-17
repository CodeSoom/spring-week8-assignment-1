package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserCreateRequestDto;
import com.codesoom.assignment.dto.UserUpdateRequestDto;
import com.codesoom.assignment.security.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.AccessDeniedException;

import javax.validation.Valid;

/**
 * 사용자 관련 요청을 처리합니다.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private UserRepository userRepository;

    /**
     * 주어진 id에 해당하는 사용자의 상세 정보를 반환합니다.
     *
     * @param id 사용자의 식별자
     * @return 사용자
     */
    @GetMapping("{id}")
    public User detail(@PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * 새로운 사용자를 등록합니다.
     *
     * @param createRequest 등록할 사용자 정보
     * @return 등록된 사용자
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid UserCreateRequestDto createRequest) {
        User user = userService.createUser(createRequest);

        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * 주어진 id에 해당하는 사용자의 정보를 수정합니다.
     * 'USER' 권한을 가진 인증된 사용자만 사용자의 정보를 수정할 수 있습니다.
     *
     * @param id 수정할 사용자의 식별자
     * @param updateRequest 수정할 사용자 정보
     * @return 수정된 사용자
     */
    @RequestMapping(value = "{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    User update(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequestDto updateRequest,
            UserAuthentication authentication
    ) throws AccessDeniedException {
        Long userId = authentication.getUserId();
        User user = userService.updateUser(id, updateRequest, userId);

        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * 주어진 id에 해당하는 사용자를 삭제합니다.
     * 'ADMIN' 권한을 가진 인증된 사용자만 사용자를 삭제할 수 있습니다.
     *
     * @param id 삭제할 사용자의 식별자
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
