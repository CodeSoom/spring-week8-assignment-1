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
     * 주어진 회원으로 현재 회원을 갱신합니다.
     *
     * @param source 갱신할 회원
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 주어진 비밀번호로 현재 비밀번호를 변경합니다.
     *
     * @param password 변경할 비밀번호
     * @param passwordEncoder 암호화 도구
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 회원을 삭제 처리합니다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 주어진 비밀번호와 현재 비밀번호가 일치하고, 회원이 삭제되지 않았다면 true를 리턴합니다.
     *
     * @param password 비밀번호
     * @param passwordEncoder 암호화 도구
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
