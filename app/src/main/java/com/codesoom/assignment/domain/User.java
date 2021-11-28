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
     * 유저 정보로 유저의 이름을 수정한다.
     *
     * @param source 유저 수정 정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 제공된 비밀번호로 유저의 비밀번호를 수정한다.
     *
     * @param password 수정요청 비밀번호
     * @param passwordEncoder 유저 비밀번호 암호화 인스턴스
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 삭제 상태로 변경한다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 유저 인증을 시도한 후 성공시 true, 실패시 false를 반환한다.
     *
     * @param password 유저 비밀번호
     * @param passwordEncoder 유저 비밀번호 암호화 인스턴스
     * @return 유저 인증이 성공한 경우 true, 실패시 false
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
