package com.codesoom.assignment.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String email = "";

    private String name = "";

    private String password = "";

    private boolean deleted = false;

    protected User() {
    }

    @Builder
    public User(Long id, String email, String name, String password, boolean deleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        if(password != null) {
            this.password = password;
        }
        this.deleted = deleted;
    }

    public void changeWith(User source) {
        name = source.name;
    }

    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void destroy() {
        deleted = true;
    }

    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
