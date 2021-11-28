package com.codesoom.assignment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 회원.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Builder.Default
    private String email = "";

    @Builder.Default
    private String name = "";

    @Builder.Default
    private String password = "";

    @Builder.Default
    private boolean deleted = false;

    /**
     * 회원 정보를 변경합니다.
     *
     * @param source 변경할 회원 정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 비밀번호를 변경합니다.
     *
     * @param password        변경할 비밀번호
     * @param passwordEncoder 비밀번호 암호화 방식
     */
    public void changePassword(String password,
        PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 회원을 삭제합니다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 인증에 성공하면 true, 실패하면 false를 리턴합니다.
     *
     * @param password        인증 시 확인할 비밀번호
     * @param passwordEncoder 비밀번호 암호화 방식
     * @return 인증에 성공하면 true, 실패하면 false
     */
    public boolean authenticate(String password,
        PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
