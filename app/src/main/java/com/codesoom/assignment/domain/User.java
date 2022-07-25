package com.codesoom.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
     * 주어진 회원으로 현재 회원을 갱신한다.
     *
     * @param source 갱신할 회원
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 주어진 비밀번호로 현재 비밀번호를 변경한다.
     *
     * @param password 변경할 비밀번호
     * @param passwordEncoder 패스워드 인코더
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 회원을 삭제한다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 주어진 정보가 삭제되지 않은 상태이면서 올바른 정보인지 검증한다.
     *
     * @param password 비밀번호
     * @param passwordEncoder 패스워드 인코더
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
