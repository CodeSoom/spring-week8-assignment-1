package com.codesoom.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 사용자 정보.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @Builder.Default
    private String name = "";

    @Setter
    @Builder.Default
    private String email = "";

    @Setter
    @Builder.Default
    private String password = "";

    /**
     * 사용자가 삭제되었다면 true를, 그렇지 않다면 false를 반환한다.
     */
    @Builder.Default
    private boolean deleted = false;

    /**
     * 사용자 정보를 수정합니다.
     *
     * @param source 수정할 사용자 정보
     */
    public void updateWith(User source) {
        this.email = source.getEmail();
    }

    /**
     * 사용자 비밀번호를 주어진 비밀번호로 변경한 뒤 암호화합니다.
     *
     * @param password 변경할 비밀번호
     * @param passwordEncoder 패스워드 인코더
     */
    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 사용자를 삭제합니다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 삭제되지 않은 사용자이며, 비밀번호가 일치한다면 true를 반환합니다.
     *
     * @param password 사용자 비밀번호
     * @param passwordEncoder 패스워드 인코더
     */
    public boolean authenticate(String password, PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
