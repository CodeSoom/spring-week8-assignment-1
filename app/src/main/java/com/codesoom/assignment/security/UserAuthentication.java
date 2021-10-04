package com.codesoom.assignment.security;

import com.codesoom.assignment.domain.Role;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 유저 인증 정보
 */
public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;

    public UserAuthentication(Long userId, List<Role> roles) {
        super(authorities(roles));
        this.userId = userId;
    }

    /**
     * 유저 자격 정보를 반환한다.
     *
     * @return null
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 유저 인증 주체의 식별자를 반환한다.
     *
     * @return 유저 인증 주체의 식별자
     */
    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * 유저 식별자를 반환한다.
     *
     * @return 유저 식별자
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 유저 식별자가 포함된 authentication 문자열을 반환한다.
     *
     * @return 유저 식별자가 포함된 authentication 문자열
     */
    @Override
    public String toString() {
        return "Authentication(" + userId + ")";
    }

    /**
     *  유저 권한 정보를 바탕으로 승인된 권한 리스트를 반환한다.
     *
     * @param roles 유저 권한 정보 리스트
     * @return 승인된 유저 권한 정보 리스트
     */
    private static List<GrantedAuthority> authorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
