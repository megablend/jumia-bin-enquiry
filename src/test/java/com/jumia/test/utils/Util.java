/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumia.test.config.ApplicationProperties;
import com.jumia.test.dto.external.Bank;
import com.jumia.test.dto.external.BinListResponse;
import com.jumia.test.models.CardQueryHit;
import com.jumia.test.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Slf4j
public final class Util {
    
    private Util() {} // prevent other classes from initilization
    
    public static String generateToken(String username, ApplicationProperties properties) {
        Long now = System.currentTimeMillis();
        String token = Jwts.builder()
                        .setSubject(username)	
                        .claim("authorities", new ArrayList<>())
                        .setIssuedAt(new Date(now))
                        .setExpiration(new Date(now + properties.getSecurityJwtExpiration() * 1000))  // in milliseconds
                        .signWith(SignatureAlgorithm.HS512, properties.getSecurityJwtSecret().getBytes())
                        .compact();
        return token;
    }
    
    public static BinListResponse getSuccessMockBinResponse() {
        BinListResponse response = new BinListResponse();
        response.setScheme("mastercard");
        response.setType("debit");
        response.setBank(new Bank("GUARANTY TRUST BANK", null, null, null));
        response.setPrepaid(true);
        return response;
    }
    
    public static CardQueryHit mockCardQueryHit() {
        return new CardQueryHit(Long.valueOf(1), new User(Long.valueOf(1), "noniboycharsy@gmail.com", null, null, null, null), "435333", 2);
    }
    
    public static List<CardQueryHit> getMockedQeryHits() {
        List<CardQueryHit> hits = new ArrayList<>();
        hits.add(new CardQueryHit(Long.valueOf(1), new User(Long.valueOf(1), "noniboycharsy@gmail.com", null, null, null, null), "435333", 2));
        hits.add(new CardQueryHit(Long.valueOf(2), new User(Long.valueOf(2), "noniboycharsy1111@gmail.com", null, null, null, null), "539983", 10));
        return hits;
    }
    
    public static Specification<CardQueryHit> stubSpecification() {
        return (Root<CardQueryHit> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            return cb.equal(root.get("key"), "value");
        };
    }
    
    public static Specification<User> stubUserSpecification() {
        return (Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            return cb.equal(root.get("key"), "value");
        };
    }
    
    public static Page<CardQueryHit> stubPage() {
        Page<CardQueryHit> page = new PageImpl<>(getMockedQeryHits());
        return page;
    }
    
    public static <T> T convertStringToObject(String data, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            T obj = mapper.readValue(data, clazz);
            return obj;
        } catch (IOException e) {
            log.error("Unable to parse json string {}", data, e);
        }
        return null;
    }
}
