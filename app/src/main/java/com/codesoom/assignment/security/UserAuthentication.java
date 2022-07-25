package com.codesoom.assignment.security;

import com.codesoom.assignment.domain.Role;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User 인증 정보
 */
public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;

    public UserAuthentication(Long userId, List<Role> roles) {
        super(authorities(roles));
        this.userId = userId;
    }

    /**
     * User 자격 정보 반환
     * @return null
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * User id 반환
     *
     * @return User id
     */
    @Override
    public Object getPrincipal() {
        return userId;
    }

    /**
     * 인증된 User 인 경우 true 반환
     *
     * @return true
     */
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * User id 반환
     *
     * @return id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * User id 가 포함된 문자열 반환
     *
     * @return id 가 포함된 문자열
     */
    @Override
    public String toString() {
        return "Authentication(" + userId + ")";
    }

    /**
     * User 권한 정보로 승인된 User 권한 목록 반환
     *
     * @param roles User 권한 정보
     * @return 승인된 User 권한 정보
     */
    private static List<GrantedAuthority> authorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
