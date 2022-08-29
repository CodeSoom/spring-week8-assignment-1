package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import lombok.Getter;

import java.util.Objects;

@Getter
public class UserInquiryInfo {
    private final Long id;
    private final String email;
    private final String name;
    private final Role role;

    public UserInquiryInfo(Long id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static UserInquiryInfo from(User user) {
        return new UserInquiryInfo(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserInquiryInfo that = (UserInquiryInfo) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(name, that.name) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, role);
    }
}
