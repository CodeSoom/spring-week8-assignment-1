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
     * 이름을 변경한다.
     *
     * @param source - 변경할 정보가 담긴 User 객체
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /**
     * 비밀번호를 변경한다.
     *
     * @param password - 변경할 비밀번호(평문)
     * @param passwordEncoder - 비밀번호 암호화 인코더
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 유저 삭제
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 삭제 여부 및 비밀번호 일치 여부 확인
     *
     * @param password - 비밀번호 (평문)
     * @param passwordEncoder - 비밀번호 암호화 인코더
     * @return 삭제 및 비밀번호 일치 여부.
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
