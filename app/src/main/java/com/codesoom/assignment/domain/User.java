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

    /**
     * 삭제(탈퇴) 여부
     */
    @Builder.Default
    private boolean deleted = false;

    /**
     * 회원의 정보를 변경합니다.
     *
     * @param source 갱신할 회원 정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 회원의 비밀번호를 암호화하여 변경합니다.
     *
     * @param password        평문 패스워드
     * @param passwordEncoder 패스워드 인코더
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 회원을 삭제(탈퇴)처리 합니다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 삭제(탈퇴)하지 않은 사용자이며 패스워드가 일치하면 true, 아니면 false를 반환합니다.
     *
     * @param password 사용자 비밀번호
     * @param passwordEncoder 패스워드 인코더
     * @return 인증 여부
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
