package com.codesoom.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 사용자
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
     * 사용자 정보를 갱신한다.
     *
     * @param source 갱신할 회원 정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 비밀번호를 갱신한다.
     *
     * @param password 갱신할 비밀번호
     * @param passwordEncoder 비밀번호 암호화 방식
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 사용자를 삭제한다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 사용자를 인증결과를 리턴한다.
     *
     * @param password 사용자 비밀번호
     * @param passwordEncoder 비밀번호 암호화 방식
     * @return 인증 결과
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
