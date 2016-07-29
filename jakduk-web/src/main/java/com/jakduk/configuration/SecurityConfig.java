package com.jakduk.configuration;

import com.jakduk.configuration.authentication.AuthenticationTokenFilter;
import com.jakduk.configuration.authentication.JakdukDetailsService;
import com.jakduk.configuration.authentication.SocialDetailService;
import com.jakduk.configuration.authentication.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * Created by pyohwan on 16. 4. 6.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)

                /*
                //Configures form login
                .formLogin()
                    .loginProcessingUrl("/api/login")
                    .successHandler(jakdukSuccessHandler())
                    .failureHandler(jakdukFailureHandler())
                //Configures the logout function

                .and()
                    .logout()
                        .logoutSuccessHandler(restLogoutSuccessHandler())
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")

//                .and()
//                    .httpBasic()                // basic auth 사용.
                .and()
                */

                .exceptionHandling()
                    .authenticationEntryPoint(restAuthenticationEntryPoint())
                    .accessDeniedHandler(restAccessDeniedHandler())

                //Configures url based authorization
                .and()

                .authorizeRequests()
                    .antMatchers(
                        "/check/**",
                        "/api/logout",
                        "/home/**",
                        "/about/**",
                        "/auth/**"
                    ).permitAll()
                    .antMatchers(
                        "/api/login",           // 로그인
                        "/auth/*",              // SNS 인증
                        "/signup",              // SNS 계정으로 회원 가입
                        "/user/social",         // OAUTH2 콜백
                        "/user/write",          // JakduK 회원 가입
                        "/user/*/write",        // SNS 계정으로 회원 가입
                        "/password/*"           // 비밀번호 찾기
                    ).anonymous()
                    .antMatchers(
                        "/user/**",
//                      "/swagger-ui.html",     // 스웨거
                        "/rest/**"              // spring-data-rest
                        ).authenticated()

                    // RESTful로 전환했기 때문에 바뀌어야 한다
                    .antMatchers(
                        "/board/*/write",
                        "/board/*/edit",
                        "/jakdu/write"
                    ).hasAnyRole("USER_01", "USER_02", "USER_03")
                    .antMatchers(
                        "/admin/**",
                        "/api/admin/**"
                    ).hasRole("ROOT")
                    .anyRequest().permitAll()

                .and()
                .rememberMe().key("jakduk_cookie_key_auto_login")

                .and()
                .apply(new SpringSocialConfigurer())

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jakdukDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }

    @Bean
    public SocialUserDetailsService socialUsersDetailService() {
        return new SocialDetailService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

    @Bean
    public JakdukSuccessHandler jakdukSuccessHandler() {
        return new JakdukSuccessHandler();
    }

    @Bean
    public JakdukFailureHandler jakdukFailureHandler() {
        return new JakdukFailureHandler();
    }

    @Bean
    public RestLogoutSuccessHandler restLogoutSuccessHandler() {
        return new RestLogoutSuccessHandler();
    }

    @Bean
    public RestAccessDeniedHandler restAccessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public JakdukDetailsService jakdukDetailsService() {
        return new JakdukDetailsService();
    }

}
