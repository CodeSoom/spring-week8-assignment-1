package com.codesoom.assignment.domain.users;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @ColumnDefault("false")
    @Column(insertable = false)
    private Boolean deleted = Boolean.FALSE;

    @Column(insertable = false)
    private LocalDateTime deletedAt;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "USER_ID")
    private List<Role> roles;

    protected User() {
    }

    private User(String name, String email, List<Role> roles) {
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public static User of(String name, String email) {
        return new User(name, email, Arrays.asList(new Role(UserRole.USER)));
    }

    public static User of(String name, String email, List<Role> roles) {
        return new User(name, email, roles);
    }

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User update(User user) {
        this.name = user.name;
        this.email = user.email;
        return this;
    }

    /**
     * 주어진 비밀번호를 암호화 처리 후 password 필드에 초기화 합니다.
     *
     * @param rawPassword 입력받은 비밀번호
     * @param passwordEncoder 비밀번호 암호화 인코더
     */
    public void changePassword(String rawPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(rawPassword);
    }

    public boolean authenticate(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    /** 이 회원을 삭제 상태로 변경하고, 변경된 회원을 리턴합니다. */
    public User destroy() {
        this.deleted = Boolean.TRUE;
        this.deletedAt = LocalDateTime.now();
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isDeleted() {
        return deleted;
    }

}
