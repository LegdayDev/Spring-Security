package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize,postAuthorize 어노테이션 활성화
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있다.
            .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
            .anyRequest().permitAll()
            .and()
            .formLogin()
            .loginPage("/loginForm")
            .loginProcessingUrl("/login") // /login 경로가 호출되면 시큐리티가 권한을 가져 로그인 진행
            .defaultSuccessUrl("/") // login 성공시 메인페이지로 이동
            .and()
            .oauth2Login()
            .loginPage("/loginForm")
            // 로그인이 완료되면 후처리가 필요하다. (엑세스 토큰 + 사용자프로필 정보)를 받는다.
            .userInfoEndpoint()
            .userService(principalOauth2UserService);

        return http.build();
    }
}
