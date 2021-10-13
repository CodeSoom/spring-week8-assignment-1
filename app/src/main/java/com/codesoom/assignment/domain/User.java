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
 * 회원 정보
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
     * 회원의 이름을 변경한다.
     *
     * @param source 변경할 회원 정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 새로운 비밀번호를 암호화하여 변경한다.
     *
     * @param password 새로운 비밀 번호
     * @param passwordEncoder 비밀번호 암호화 인터페이스
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 회원의 삭제상태를 참으로 변경한다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 회원 정보와 인자 값을 비교 후 일치 여부를 반환한다.
     *
     * @param password 비교 할 비밀 번호
     * @param passwordEncoder 비밀 번호 암호화 인터페이스
     * @return
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
