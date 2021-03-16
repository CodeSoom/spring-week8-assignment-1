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
 * 회원 정보.
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
     * 회원의 정보를 변경합니다.
     * @param source 변경할 사용자 정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 회원의 비밀번호를 암호화합니다.
     * @param password 암호화 되지 않은 회원의 비밀번호
     * @param passwordEncoder 패스워드 인코더
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
     * 회원이 삭제되지 않았고, 평문 비밀번호와 인코딩 된 비밀번호가 일치하면 true를 반환합니다.
     *
     * @param password 비밀번호
     * @param passwordEncoder 패스워드 인코더
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
