package com.codesoom.assignment.security;

import com.codesoom.assignment.domain.Role;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;

    public UserAuthentication(Long userId, List<Role> roles) {
        super(authorities(roles));
        this.userId = userId;
    }

    /**
     * 사용자의 자격을 리턴한다.
     *
     * @return null
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 사용자 id를 리턴합니다.
     *
     * @return 사용자 id
     */
    @Override
    public Object getPrincipal() {
        return userId;
    }

    /**
     * 인증한된 사용자는 true를 리턴한다.
     *
     * @return true
     */
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * 사용자 id를 리턴한다.
     *
     * @return 사용자 id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * id가 포함된 인증정보를 리턴한다.
     *
     * @return 사용자 id가 포함된 문자
     */
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
