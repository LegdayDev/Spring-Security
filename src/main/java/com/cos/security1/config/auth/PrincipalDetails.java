package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <h2>스프링 시큐리티 세션 저장</h2>
 * <li>로그인이 완료되면 Session 공간 안에 Security ContextHolder 라는 공간에 로그인 정보를 저장</li>
 * <li>Security ContextHolder 안에 Authentication 타입으로 감싸져 있다.</li>
 * <li>Authentication 객체 안에 User 정보가 있어야 하고</li>
 * <li>User 오브젝트 타입은 UserDetails 타입 객체이다.</li>
 *
 * <li>UserDetails 는 인터페이스이므로 구현체를 만들어서 로그인 로직을 짜면 된다.</li>
 */
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    /**
     * 해당 User 의 권한을 리턴하는 메서드
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 계정이 만료 되었는지
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨있는지 ?
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 게정 비밀번호가 오래사용됐는지 ?
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 비활성화 되어있는지 ?
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
