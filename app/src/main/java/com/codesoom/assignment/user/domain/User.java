package com.codesoom.assignment.user.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;

    @Builder.Default
    private boolean deleted = false;

    protected User() {
        this.deleted = false;
    }

    @Builder
    private User(final Long id, final String email,
                 final String name, final String password, final boolean deleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.deleted = deleted;
    }

    /**
     * 회원 정보를 수정합니다. <br>
     * 필드가 null일 경우 수정하지 않습니다.
     *
     * @param updateUser 수정할 회원 정보
     */
    public void update(final User updateUser) {
        updateName(updateUser.getName());
        updatePassword(updateUser.getPassword());
    }

    /**
     * 회원의 비밀번호를 수정합니다. <br>
     * 비밀번호는 PasswordEncoder를 통해 인코딩됩니다.
     *
     * @param password        회원 비밀번호
     * @param passwordEncoder 비밀번호 인코더
     */
    public void changePassword(final String password,
                               final PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 회원을 삭제합니다. <br>
     * 실제 값을 삭제하지 않고 deleted 변수를 true로 변환합니다.
     */
    public void destroy() {
        this.deleted = true;
    }

    /**
     * 회원 인증을 진행합니다.
     *
     * @param password        인증할 회원 비밀번호
     * @param passwordEncoder 비밀번호 인코더
     * @return 회원 인증 성공/실패 여부를 리턴
     */
    public boolean authenticate(final String password,
                                final PasswordEncoder passwordEncoder) {
        return !this.deleted && passwordEncoder.matches(password, this.password);
    }


    private void updateName(final String name) {
        if (!name.isBlank()) {
            this.name = name;
        }
    }

    private void updatePassword(final String password) {
        if (!password.isBlank()) {
            this.password = password;
        }
    }
}
