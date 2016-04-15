package com.jakduk.configuration;

import com.jakduk.authentication.jakduk.JakdukDetailsService;
import com.jakduk.authentication.jakduk.JakdukFailureHandler;
import com.jakduk.authentication.jakduk.JakdukSuccessHandler;
import com.jakduk.authentication.social.SocialDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
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
@ImportResource(value = {
//        "classpath:/security-context.xml",
        "classpath:/config/oauth/oauth-data.xml"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //http.addFilterAfter()

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
                                "/login*",
                                "/auth/*",
                                "/signup/*",
                                "/user/write",
                                "/oauth"
                        ).anonymous()
                        .antMatchers(
                                "/user/**",
                                "/oauth/**"
                        ).authenticated()
                        .antMatchers(
                                "/board/*/write",
                                "/board/*/edit",
                                "/jakdu/write"
                        ).hasAnyRole("USER_01", "USER_02", "USER_03")
                        .antMatchers("/admin/**").hasRole("ROOT")
                        .anyRequest().permitAll()
                .and()
                    .apply(new SpringSocialConfigurer())
                .and()
                    .sessionManagement()
                        .maximumSessions(3).expiredUrl("/error/maxSession");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jakdukDetailsService())
                .passwordEncoder(passwordEncoder());

        //auth.inMemoryAuthentication().withUser("test06@test.com").password("password").roles("ADMIN");
    }

    @Bean
    public SocialUserDetailsService socialUsersDetailService() {
        return new SocialDetailService();
    }


    /*
    @Bean
    public DaoAuthenticationProvider jakdukAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(jakdukDetailsService);
        return provider;
    }
    */

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
