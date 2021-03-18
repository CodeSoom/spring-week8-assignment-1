package com.codesoom.assignment.security;

import com.codesoom.assignment.domain.Role;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 인증 정보.
 */
public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;

    public UserAuthentication(Long userId, List<Role> roles) {
        super(authorities(userId, roles));
        this.userId = userId;
    }

    /**
     * 사용자 자격 정보를 반환합니다.
     *
     * @return 사용자 자격 정보
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 사용자 인증 객체를 반환합니다.
     *
     * @return 사용자 인증 객체
     */
    @Override
    public Object getPrincipal() {
        return userId;
    }

    /**
     * 인증된 사용자라면 true를 반환합니다.
     */
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * 사용자 식별자를 반환합니다.
     *
     * @return 사용자 식별자
     */
    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Authentication{" + userId + "}";
    }

    /**
     * 인증된 사용자의 권한 목록을 반환합니다.
     */
    private static List<GrantedAuthority> authorities(
            Long userId, List<Role> roles) {
       return roles.stream()
               .map(role -> new SimpleGrantedAuthority(role.getName()))
               .collect(Collectors.toList());
    }
}
