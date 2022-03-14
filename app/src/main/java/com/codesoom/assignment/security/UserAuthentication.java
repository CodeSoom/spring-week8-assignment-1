package com.codesoom.assignment.security;

import com.codesoom.assignment.domain.Role;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 인증 정보
 */
public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;

    public UserAuthentication(Long userId, List<Role> roles) {
        super(authorities(roles));
        this.userId = userId;
    }

    /**
     * 사용자 자격 정보를 리턴한다.
     *
     * @return null
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 사용자 아이디를 리턴합니다.
     *
     * @return 사용자 아이디
     */
    @Override
    public Object getPrincipal() {
        return userId;
    }

    /**
     * 인증한 사용자면 true를 리턴한다.
     *
     * @return 인증한 사용자 true
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
     * 사용자 id가 포함된 문자열을 리턴한다.
     *
     * @return 사용자 id가 포함된 문자열
     */
    @Override
    public String toString() {
        return "Authentication(" + userId + ")";
    }

    /**
     * 사용자 권한 정보로 승인된 권한 리스트를 리턴한다.
     *
     * @param roles 사용자 권한 정보
     * @return 승인된 사용자 권한 정보
     */
    private static List<GrantedAuthority> authorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
