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
 * 유저 모델
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
     * 유저 이름을 저장한다.
     * @param source
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 유저 비밀번호를 변경한다.
     * @param password 비밀번호
     * @param passwordEncoder
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 유저가 삭제되었다는 표시를 한다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 인증 정보를 관리한다.
     * @param password 비밀번호
     * @param passwordEncoder
     * @return
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
