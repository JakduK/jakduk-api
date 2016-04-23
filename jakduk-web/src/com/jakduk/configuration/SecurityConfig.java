package com.jakduk.configuration;

import com.jakduk.authentication.jakduk.JakdukDetailsService;
import com.jakduk.authentication.jakduk.JakdukFailureHandler;
import com.jakduk.authentication.jakduk.JakdukSuccessHandler;
import com.jakduk.authentication.social.SocialDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
                //                .csrf().disable()
                //Configures form login
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    .successHandler(jakdukSuccessHandler())
                    .failureHandler(jakdukFailureHandler())
                //Configures the logout function
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/logout/success")
                .and()
                    .exceptionHandling().accessDeniedPage("/error/denied")
                //Configures url based authorization
                .and()
                    .authorizeRequests()
                        .antMatchers(
                                "/check/**",
                                "/logout",
                                "/home/**",
                                "/about/**",
                                "/auth/**"
                        ).permitAll()
                        .antMatchers(
                                "/login*",          // 로그인
                                "/auth/*",          // SNS 인증
                                "/signup",          // SNS 계정으로 회원 가입
                                "/user/social",
                                "/user/write",      // JakduK 회원 가입
                                "/user/*/write"     // SNS 계정으로 회원 가입
                        ).anonymous()
                        .antMatchers(
                                "/user/**"
                        ).authenticated()
                        .antMatchers(
                                "/board/*/write",
                                "/board/*/edit",
                                "/jakdu/write"
                        ).hasAnyRole("USER_01", "USER_02", "USER_03")
                        .antMatchers("/admin/**").hasRole("ROOT")
                        .anyRequest().permitAll()
                .and()
                    .apply(getSpringSocialConfigurer())
                .and()
                    .sessionManagement()
                        .maximumSessions(3).expiredUrl("/error/maxSession");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jakdukDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    // 로그인 성공 후 특정 URL일 경우 REDIRECT 안 시키는 로직을 추가 해야 한다.
    private SpringSocialConfigurer getSpringSocialConfigurer() {
        SpringSocialConfigurer configurer = new SpringSocialConfigurer();
        return  configurer;
    }

    @Bean
    public SocialUserDetailsService socialUsersDetailService() {
        return new SocialDetailService();
    }

    @Bean
    public StandardPasswordEncoder passwordEncoder() {
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
    public JakdukDetailsService jakdukDetailsService() {
        return new JakdukDetailsService();
    }
}
