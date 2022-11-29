package com.codesoom.assignment.support;

import com.codesoom.assignment.user.adapter.in.web.dto.request.UserCreateRequestDto;
import com.codesoom.assignment.user.adapter.in.web.dto.request.UserUpdateRequestDto;
import com.codesoom.assignment.user.domain.User;

import static com.codesoom.assignment.support.IdFixture.ID_MAX;

public enum UserFixture {
    회원_1번(1L, "기범", "dev.gibeom@gmail.com", "비밀번호486"),
    회원_2번(2L, "Alex", "kpmyung@gmail.com", "password486"),
    회원_이름_비정상(ID_MAX.value(), "", "notExistName@no.name", "이름이 없어요"),
    회원_이메일_비정상(ID_MAX.value(), "이메일이 공백이예요", "", "이메일이 없어요"),
    회원_비밀번호_비정상(ID_MAX.value(), "비밀번호가 2글자예요", "passwordInvalid@invalid.password", "hi"),
    회원_1번_틀린_비밀번호(1L, "기범", "dev.gibeom@gmail.com", "비밀번호 틀릴거지롱"),
    ;

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    UserFixture(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public User 회원_엔티티_생성() {
        return 회원_엔티티_생성(null);
    }

    public User 회원_엔티티_생성(Long id) {
        return User.builder()
                .id(id)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .build();
    }

    public UserCreateRequestDto 등록_요청_데이터_생성() {
        return UserCreateRequestDto.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .build();
    }

    public UserUpdateRequestDto 수정_요청_데이터_생성() {
        return UserUpdateRequestDto.builder()
                .name(this.name)
                .password(this.password)
                .build();
    }

    public Long 아이디() {
        return this.id;
    }

    public String 이름() {
        return this.name;
    }

    public String 이메일() {
        return this.email;
    }

    public String 비밀번호() {
        return this.password;
    }
}
