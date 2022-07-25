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
 * 회원 Entity
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
     * 회원 정보를 수정한다.
     * 
     * @param source 수정할 정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 비밀번호를 암호화한다.
     * 
     * @param password 회원 비밀번호
     * @param passwordEncoder 암호화에 사용할 인코더
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 삭제 여부를 true로 바꾼다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 비밀번호 인증에 성공하면 true를 반환한다.
     * 
     * @param password 회원 비밀번호
     * @param passwordEncoder 인증에 사용할 인코더
     * @return 비밀번호 인증에 성공하면 true, 실패하면 false를 반환한다.
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
