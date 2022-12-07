package com.codesoom.assignment.adapter.in.web.user;

import com.codesoom.assignment.adapter.in.web.user.dto.request.UserCreateRequestDto;
import com.codesoom.assignment.adapter.in.web.user.dto.request.UserUpdateRequestDto;
import com.codesoom.assignment.adapter.in.web.user.dto.response.CreateUserResponseDto;
import com.codesoom.assignment.adapter.in.web.user.dto.response.UpdateUserResponseDto;
import com.codesoom.assignment.common.security.UserAuthentication;
import com.codesoom.assignment.user.application.port.in.UserUseCase;
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

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserUseCase userUseCase;

    public UserController(final UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    /**
     * 회원을 등록하고 리턴합니다.
     *
     * @param userCreateRequestDto 등록할 회원 정보
     * @return 등록한 회원 상세 정보 리턴
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateUserResponseDto create(@RequestBody @Valid final UserCreateRequestDto userCreateRequestDto) {
        return new CreateUserResponseDto(
                userUseCase.createUser(userCreateRequestDto)
        );
    }

    /**
     * 회원을 수정하고 리턴합니다.
     *
     * @param id                   회원 고유 id
     * @param userUpdateRequestDto 수정할 회원 정보
     * @param authentication       사용자 인증 정보
     * @return 수정한 회원 상세 정보 리턴
     * @throws AccessDeniedException 요청 id와 인증 정보 id가 서로 다를 경우
     */
    @PatchMapping("{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    UpdateUserResponseDto update(@PathVariable final Long id,
                                 @RequestBody @Valid final UserUpdateRequestDto userUpdateRequestDto,
                                 final UserAuthentication authentication) throws AccessDeniedException {
        return new UpdateUserResponseDto(
                userUseCase.updateUser(id, userUpdateRequestDto, authentication.getUserId())
        );
    }

    /**
     * 회원을 삭제합니다. <br>
     * 실제 값을 삭제하지 않고 deleted 변수를 true로 변환합니다.
     *
     * @param id 회원 고유 id
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    void destroy(@PathVariable final Long id) {
        userUseCase.deleteUser(id);
    }
}
