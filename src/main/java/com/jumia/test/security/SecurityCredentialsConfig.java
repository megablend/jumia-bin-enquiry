/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.security;

import com.jumia.test.config.ApplicationProperties;
import com.jumia.test.filters.JwtUsernameAndPasswordAuthenticationFilter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author cmegafu
 */
@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ApplicationProperties appProperties;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> {
                    rsp.setContentType("application/json");
                    rsp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    rsp.getOutputStream().println("{ \"error\": \"" + e.getMessage() + "\" }");
                }).and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), appProperties))
                .antMatcher(appProperties.getSecurityJwtUri())
                .authorizeRequests()
		    // allow all POST requests
		    .antMatchers(HttpMethod.POST, appProperties.getSecurityJwtUri()).permitAll()
		    // any other requests must be authenticated
		    .anyRequest().authenticated();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
