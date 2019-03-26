/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumia.test.config.ApplicationProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Slf4j
public final class Util {
    
    private Util() {} // prevents this class from being initialized
    
    public static void authResponse(HttpServletResponse response, String authenticationKey, String token) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put(authenticationKey, token);
        try {
            String content = mapper.writeValueAsString(map);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write(content);
        } catch (IOException e) {
            log.error("Unable to get authentication token", e);
        }
    }
    
    public static String generateToken(Authentication auth, ApplicationProperties properties) {
        Long now = System.currentTimeMillis();
        String token = Jwts.builder()
                        .setSubject(auth.getName())	
                        .claim("authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .setIssuedAt(new Date(now))
                        .setExpiration(new Date(now + properties.getSecurityJwtExpiration() * 1000))  // in milliseconds
                        .signWith(SignatureAlgorithm.HS512, properties.getSecurityJwtSecret().getBytes())
                        .compact();
        return token;
    }
}
