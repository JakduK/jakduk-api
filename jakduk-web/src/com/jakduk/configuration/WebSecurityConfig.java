package com.jakduk.configuration;

import com.jakduk.authentication.jakduk.JakdukFailureHandler;
import com.jakduk.authentication.jakduk.JakdukSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by pyohwan on 16. 4. 6.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JakdukSuccessHandler jakdukSuccessHandler;

    @Autowired
    private JakdukFailureHandler jakdukFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/check/**", "/logout", "/home/**", "/about/**").permitAll()
                    .antMatchers("/login*", "/j_spring_*", "/user/write").anonymous()
                    .antMatchers("/user/**", "/oauth/**").authenticated()
                    .antMatchers("/board/*/write", "/board/*/edit", "/jakdu/write").hasAnyRole("ROLE_USER_01", "ROLE_USER_02", "ROLE_USER_03")
                    .antMatchers("/admin/**").hasRole("ROLE_ROOT")
                    .anyRequest().anonymous()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("j_username")
                    .usernameParameter("j_password")
                    .successHandler(jakdukSuccessHandler)
                    .failureHandler(jakdukFailureHandler)
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/logout/success")
                    .and()
                .csrf().disable()
                .httpBasic();

    }
}
