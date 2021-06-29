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
 * 회원정보
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 회원정보의 고유 ID값
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 회원의 Email
     */
    @Builder.Default
    private String email = "";

    /**
     * 회원의 이름
     */
    @Builder.Default
    private String name = "";

    /**
     * 회원정보 비밀번호
     */
    @Builder.Default
    private String password = "";

    /**
     * 회원정보 비활성화 여부
     */
    @Builder.Default
    private boolean deleted = false;

    /**
     * 회원의 이름을 변경합니다.
     *
     * @param source 변경할 회원정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 회원정보의 비밀번호를 변경합니다.
     *
     * @param password 변경할 회원번호 비밀번호
     * @param passwordEncoder 비밀번호 Encoder
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 회원정보를 비활성화 합니다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 로그인 시 올바른 비밀번호 인지 판별합니다.
     *
     * @param password 입력받은 비밀번호
     * @param passwordEncoder 비밀번호 Encoder
     * @return 비밀번호 판별 결과
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
