/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.filters;

import com.jumia.test.config.ApplicationProperties;
import static com.jumia.test.util.FilterUtils.doErrorResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author cmegafu
 */
@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    
    private final ApplicationProperties appProperties;

    public JwtTokenAuthenticationFilter(ApplicationProperties appProperties) {
        this.appProperties = appProperties;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(appProperties.getSecurityJwtHeader());
        
        if (null == header || !header.startsWith(appProperties.getSecurityJwtPrefix())) {
            doErrorResponse(response, "Authorization not found in request header", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        String token = header.replace(appProperties.getSecurityJwtPrefix(), "");
        Claims claims = null;
        String username = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(appProperties.getSecurityJwtSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            username = claims.getSubject();
            if (null == username) {
                doErrorResponse(response, "Invalid token provided", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
            List<String> authorities = (List<String>) claims.get("authorities");
            if (null == authorities || authorities.isEmpty()) {
                doErrorResponse(response, "Invalid user roles", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
            // set the security details
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            chain.doFilter(request, response);
        } catch (SignatureException | MalformedJwtException e) {
            log.error("Invalid token provided", e);
            SecurityContextHolder.clearContext();
            doErrorResponse(response, "Invalid token provided", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Request Error", e);
            SecurityContextHolder.clearContext();
            doErrorResponse(response, "Unable to process request, please try again", HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
