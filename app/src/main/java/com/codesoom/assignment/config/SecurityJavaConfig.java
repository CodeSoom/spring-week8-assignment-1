package com.codesoom.assignment.config;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.filters.AuthenticationErrorFilter;
import com.codesoom.assignment.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.servlet.Filter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Filter authenticationFilter = new JwtAuthenticationFilter(
                authenticationManager(), authenticationService);
        Filter authenticationErrorFilter = new AuthenticationErrorFilter();

        http
                .csrf().disable()
                .headers()
                .frameOptions().disable()
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(authenticationErrorFilter,
                        JwtAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }
}
