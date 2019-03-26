/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumia.test.config.ApplicationProperties;
import com.jumia.test.dto.UserCredential;
import static com.jumia.test.util.Util.authResponse;
import static com.jumia.test.util.Util.generateToken;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author cmegafu
 */
@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private final ApplicationProperties appProperties;
    private final AuthenticationManager authManager;
    
    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, ApplicationProperties appProperties) {
        this.authManager = authManager;
        this.appProperties = appProperties;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth", RequestMethod.POST.toString()));
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserCredential credentials = new ObjectMapper().readValue(request.getInputStream(), UserCredential.class);
//            log.info("The request from the user is {}", credentials);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), Collections.emptyList());
            return authManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        String token = generateToken(auth, appProperties);
        // Add token to header
        authResponse(response, appProperties.getSecurityJwtTokenHeader(), token);
    }
}
