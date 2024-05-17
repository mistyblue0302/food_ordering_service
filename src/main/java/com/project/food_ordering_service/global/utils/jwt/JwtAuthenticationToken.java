package com.project.food_ordering_service.global.utils.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT 인증 처리를 담당하는 클래스
 */

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal; // 인증된 사용자정보

    public JwtAuthenticationToken(
            Object principal, Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
