package com.jakduk.api.configuration;

import com.jakduk.api.configuration.authentication.AuthenticationTokenFilter;
import com.jakduk.api.configuration.authentication.JakdukDetailsService;
import com.jakduk.api.configuration.authentication.SocialDetailService;
import com.jakduk.api.configuration.authentication.handler.RestAccessDeniedHandler;
import com.jakduk.api.configuration.authentication.handler.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author pyohwan
 * 16. 4. 6 오후 9:51
 */

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JakdukDetailsService jakdukDetailsService;

    @Autowired
    private SocialDetailService socialDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                    .authenticationEntryPoint(restAuthenticationEntryPoint())
                    .accessDeniedHandler(restAccessDeniedHandler())

                //Configures url based authorization
                .and()

                .authorizeRequests()
                    .antMatchers(

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

    @Bean
    public RestAccessDeniedHandler restAccessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

}
