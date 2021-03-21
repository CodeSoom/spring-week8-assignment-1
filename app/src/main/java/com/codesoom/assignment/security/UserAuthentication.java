package com.codesoom.assignment.security;

import com.codesoom.assignment.domain.Role;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 유저 인증 정보.
 */
public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;

    public UserAuthentication(Long userId, List<Role> roles) {
        super(authorities(roles));
        this.userId = userId;
    }

    /**
     * 유저 자격 정보를 반환합니다.
     *
     * @return 유저 자격 정보
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 유저 접근 주체 정보를 반환합니다.
     *
     * @return 유저 접근 주체
     */
    @Override
    public Object getPrincipal() {
        return userId;
    }

    /**
     * 유저 인증 여부를 반환합니다.
     *
     * @return 인증 여부
     */
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * 유저 식별자를 반환합니다.
     *
     * @return 유저 식별자
     */
    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Authentication(" + userId + ")";
    }

    private static List<GrantedAuthority> authorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
