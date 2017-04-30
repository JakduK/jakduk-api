package com.jakduk.api.configuration;

import com.jakduk.api.configuration.authentication.SnsAuthenticationProvider;
import com.jakduk.api.configuration.authentication.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author pyohwan
 * 16. 4. 6 오후 9:51
 */

@Configuration
@EnableWebSecurity
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestLogoutSuccessHandler restLogoutSuccessHandler;

    @Autowired
    private RestJakdukSuccessHandler restJakdukSuccessHandler;

    @Autowired
    private RestJakdukFailureHandler restJakdukFailureHandler;

    @Autowired
    private SnsAuthenticationProvider snsAuthenticationProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .formLogin()
                    .loginProcessingUrl("/api/auth/login")
                    .successHandler(restJakdukSuccessHandler)
                    .failureHandler(restJakdukFailureHandler)

                .and().rememberMe()

                .and().logout()
                    .logoutSuccessHandler(restLogoutSuccessHandler)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")

                .and().exceptionHandling()
                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                    .accessDeniedHandler(restAccessDeniedHandler)

                //Configures url based authorization
                .and().authorizeRequests()
                    .antMatchers(
                            HttpMethod.GET,
                            "/api/user/exist/email",                // 이메일 중복 검사
                            "/api/user/exist/username"              // 별명 중복 검사
                    ).permitAll()

                    .antMatchers(
                            HttpMethod.POST,
                            "/api/auth/user",                            // 이메일 기반 회원 가입
                            "/api/auth/user/*"                      // SNS 기반 회원 가입 및
                    ).anonymous()
                    .antMatchers(
                            HttpMethod.GET,
                            "/api/auth/login",                  // 로그인
                            "/api/auth/login/*",                    // SNS 로그인
                            "/api/user/exist/email/anonymous",      // 비 로그인 상태에서 특정 user Id를 제외하고 Email 중복 검사
                            "/api/user/exist/username/anonymous"    // 비 로그인 상태에서 특정 user Id를 제외하고 별명 중복 검사
                    ).anonymous()

                    .antMatchers(
                            HttpMethod.GET,
                            "/api/user/exist/email/edit",       // 회원 프로필 편집 시 Email 중복 검사
                            "/api/user/exist/username/edit",    // 회원 프로필 편집 시 별명 중복 검사
                            "/api/user/profile/me"              // 내 프로필 정보 보기
                    ).hasAnyRole("USER_01", "USER_02", "USER_03")
                    .antMatchers(
                            HttpMethod.POST,
                            "/api/board/free",                  // 자유게시판 글쓰기
                            "/api/board/free/comment",          // 자유게시판 댓글 달기
                            "/api/gallery"                      // 사진 올리기
                    ).hasAnyRole("USER_01", "USER_02", "USER_03")
                    .regexMatchers(
                            HttpMethod.PUT,
                            "/api/board/free/(\\d+)",           // 자유게시판 글고치기
                            "/api/user/profile/me",             // 내 프로필 정보 편집
                            "/api/user/password"                // 이메일 기반 회원의 비밀번호 변경
                    ).hasAnyRole("USER_01", "USER_02", "USER_03")
                    .regexMatchers(
                            HttpMethod.DELETE,
                            "/api/board/free/(\\d+)"            // 자유게시판 글지우기
                    ).hasAnyRole("USER_01", "USER_02", "USER_03")

                    .antMatchers(
    //                        "/restcontroller/**"                          // spring-data-restcontroller
                    ).hasRole("ROOT")

                    .anyRequest().permitAll();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(snsAuthenticationProvider);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public StandardPasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

}
