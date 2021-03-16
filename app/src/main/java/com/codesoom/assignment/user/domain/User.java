package com.codesoom.assignment.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 사용자 정보.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    /** 사용자 식별자. */
    @Id
    @GeneratedValue
    private Long id;

    /** 사용자 이메일. */
    private String email;

    /** 사용자 이름. */
    private String name;

    /** 사용자 비밀번호. */
    private String password;

    /**
     * 삭제된 사용자라면 true, 아니면 false.
     */
    private boolean deleted;

    @Builder
    public User(Long id, String email, String name, String password, boolean deleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.deleted = deleted;
    }

    public static User of(String name, String email) {
        return User.builder()
                .name(name)
                .email(email)
                .deleted(false)
                .build();
    }

    /**
     * 사용자의 정보를 갱신합니다.
     * @param source 사용자 갱신 정보
     */
    public void changeWith(User source) {
        name = source.name;
    }

    /** 사용자 비밀번호를 암호화하여 저장합니다. */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 사용자 정보를 삭제했다고 표시합니다.
     */
    public void destroy() {
        deleted = true;
    }
    /** 사용자가 삭제되지 않은 상태에서 비밀번호가 일치하면 true, 일치하지 않으면 false를 리턴한다. */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
