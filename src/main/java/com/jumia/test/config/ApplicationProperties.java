/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.config;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@ConfigurationProperties(prefix = "jumia.pay")
@Data
@Validated
public class ApplicationProperties {
    
    /**
     * Security JWT Header
     * 
     */
    @NotNull
    private String securityJwtHeader = "Authorization";
    
    /**
     * Security JWT Token Header
     * 
     */
    @NotNull
    private String securityJwtTokenHeader = "token";
    
    /**
     * Security JWT Prefix
     * 
     */
    @NotNull
    private String securityJwtPrefix = "Bearer ";
    
    /**
     * Security JWT Expiration
     * 
     */
    @NotNull
    private int securityJwtExpiration = (24 * 60 * 60);
    
    /**
     * Security JWT secret
     * 
     */
    @NotNull
    private String securityJwtSecret = "QfXbp3rKlL";
    
    /**
     * Encryption Salt
     * 
     */
    @NotNull
    private String encryptionSalt = "sg6T0g6UYQNB4wSM5O7ujCN6i1e8KI0c";
    
    /**
     * Authentication Service Path
     * 
     */
    @NotNull
    private String securityJwtUri = "/auth/**";
    
    /**
     * The cache expiry time
     */
    @NotNull
    private int expiryTime = 24;
    
    /**
     * Swagger base package
     * 
     */
    @NotNull
    private String swaggerBasePackage = "com.jumia.test.controllers";
    
    /**
     * Swagger Document Title
     * 
     */
    @NotNull
    private String swaggerDocTitle = "Jumia Pay Project Challenge";
    
    /**
     * Swagger Document Description
     * 
     */
    @NotNull
    private String swaggerDocDescription = "This microservice enabels our customers to enquire about the details of their cards. Also helps us to track the number of requests/hits made by the customers.";
    
    /**
     * Swagger Document Version
     * 
     */
    @NotNull
    private String swaggerDocVersion = "1.0.0";
    
    /**
     * The name of the developer
     */
    @NotNull
    private String developerName = "Charles Megafu";
    
    /**
     * The Profile of the developer
     * 
     */
    @NotNull
    private String developerUrl = "https://www.linkedin.com/in/charles-megafu-295a2768/";
    
    /**
     * The email of the developer
     * 
     */
    @NotNull
    private String developerEmail = "noniboycharsy@gmail.com";
    
    /**
     * Bin List Url
     * 
     */
    @NotBlank
    private String binListUrl = "https://lookup.binlist.net/";
    
    /**
     * Active MQ Broker URL
     */
    @NotBlank
    private String activemqBrokerUrl = "vm://localhost";
    
    /**
     * Active MQ Consumer Thread Pool
     * 
     */
    @NotBlank
    private String activemqConsumerThreadPool = "2-100";
    
    /**
     * ActiveMQ Delivery Retrial
     * 
     */
    private int activemqMessageDeliveryRetrial = 1;
    
    /**
     * The Active MQ topic to handle the processing of hits
     */
    @NotBlank
    private String activemqCardDetailsHitsTopic = "cardDetails";
    
    /**
     * Application Path
     * 
     */
    @NotNull
    private String path = "/card-scheme/**";
    
    /**
     * Authentication Path
     * 
     */
    @NotNull
    private String authPath = "/auth";
}
