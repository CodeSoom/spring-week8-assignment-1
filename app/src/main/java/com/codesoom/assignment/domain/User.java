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
 * 유저 정보.
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
     * 제공된 유저 정보로 수정합니다.
     *
     * @param source 수정 유저 정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 제공된 password 정보로 수정합니다.
     *
     * @param password        유저 password
     * @param passwordEncoder 유저 password 부호기
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 삭제 상태로 변경합니다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 제공된 인증 정보로 수행한 인증 성공 여부를 반환합니다.
     *
     * @param password        유저 password
     * @param passwordEncoder 유저 password 부호기
     * @return 유저 인증 성공 여부
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
