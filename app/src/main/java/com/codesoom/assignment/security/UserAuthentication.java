package com.codesoom.assignment.security;

import com.codesoom.assignment.domain.Role;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authentication For User
 *
 * @author sim
 */
public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;

    /**
     * UserAuthentication 생성자 메서드
     * 
     * @param userId - 유저 고유 식별 ID
     * @param roles - 유저 권한 리스트
     */
    public UserAuthentication(Long userId, List<Role> roles) {
        super(authorities(roles));
        this.userId = userId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Authentication(" + userId + ")";
    }

    /**
     * 권한(Role) 리스트를 GrantedAuthority 리스트로 변환한다.
     * 변환하는 이유는 AbstractAuthenticationToken에 권한 인가 정보를 넣는 자료형과 일치시키기 위함이다.
     *
     * @param roles - 권한 인가 리스트
     * @return 인가 정보가 담긴 GrantedAuthority 리스트
     */
    private static List<GrantedAuthority> authorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
