package com.codesoom.assignment.domain;

import com.codesoom.assignment.dto.UserRegisterRequest;
import com.codesoom.assignment.utils.EncryptionUtil;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_email", columnNames = "email")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private final List<Product> productList = new ArrayList<>();

    protected User() {}

    @Builder
    public User(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(UserRegisterRequest request) {
        this.email = request.getEmail();
        this.password = EncryptionUtil.encrypt(request.getPassword());
        this.name = request.getName();
    }

    public void change(String password) {
        this.password = password;
    }

    /**
     * 관리자 권한을 부여한다.
     */
    public void giveAdminPrivileges() {
        this.role = Role.ADMIN;
    }

    /**
     * 같은 유저인지 확인하고 결과를 리턴한다.
     *
     * @param id 비교할 유저 식별자
     * @return 같으면 true, 다르면 false
     */
    public boolean isSameUser(Long id) {
        return Objects.equals(this.id, id);
    }
}
