/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.security;

import com.jumia.test.config.ApplicationProperties;
import com.jumia.test.filters.JwtTokenAuthenticationFilter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author cmegafu
 */
@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ApplicationProperties appProperties;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/webjars/**", "/swagger-ui.html");
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
                // Add a filter to validate the tokens with every request
		.addFilterAfter(new JwtTokenAuthenticationFilter(appProperties), UsernamePasswordAuthenticationFilter.class)
                // authorization requests config
		.authorizeRequests()
                // allow all who are accessing "auth" service
                .antMatchers(HttpMethod.POST, appProperties.getSecurityJwtUri()).permitAll()
                .antMatchers("/v2/api-docs", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/webjars/**", "/swagger-ui.html").permitAll() // enable all the swagger resource paths
                // The bank must be a user before accessing the /api endpoint
                .antMatchers(appProperties.getPath()).hasRole("USER")
                .anyRequest().authenticated();
    }
}
