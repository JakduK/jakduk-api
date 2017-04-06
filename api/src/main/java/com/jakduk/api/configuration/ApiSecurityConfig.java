package com.jakduk.api.configuration;

import com.jakduk.api.configuration.authentication.AuthenticationTokenFilter;
import com.jakduk.api.configuration.authentication.JakdukDetailsService;
import com.jakduk.api.configuration.authentication.SocialDetailService;
import com.jakduk.api.configuration.authentication.handler.RestAccessDeniedHandler;
import com.jakduk.api.configuration.authentication.handler.RestAuthenticationEntryPoint;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author pyohwan
 * 16. 4. 6 오후 9:51
 */

@Configuration
@EnableWebSecurity
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JakdukDetailsService jakdukDetailsService;

    @Autowired
    private SocialDetailService socialDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                // Custom JWT based security filter
                .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)

                //Configures url based authorization
                .and()
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/api/user/exist/email",                // 이메일 중복 검사
                        "/api/user/exist/username"              // 별명 중복 검사
                ).permitAll()

                .antMatchers(
                        HttpMethod.POST,
                        "/api/user",                            // 이메일 기반 회원 가입
                        "/api/user/social"                      // SNS 기반 회원 가입
                ).anonymous()
                .antMatchers(
                        HttpMethod.GET,
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

                .anyRequest().permitAll()

                // don't create session
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jakdukDetailsService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(socialDetailService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter() throws Exception {
        return new AuthenticationTokenFilter();
    }
}
