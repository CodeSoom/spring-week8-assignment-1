package com.codesoom.assignment.security;

import com.codesoom.assignment.domain.Role;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 회원 인증 정보.
 */
public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;

    public UserAuthentication(Long userId, List<Role> roles) {
        super(authorities(roles));
        this.userId = userId;
    }

    /**
     * 회원 자격 정보를 반환합니다.
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 회원 인증 객체를 반환합니다.
     */
    @Override
    public Object getPrincipal() {
        return userId;
    }

    /**
     * 인증된 회원이라면 true를 리턴합니다.
     */
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * 회원 식별자를 반환합니다.
     */
    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Authentication(" + userId + ")";
    }

    /**
     * 인증된 회원의 권한을 반환합니다.
     */
    private static List<GrantedAuthority> authorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
