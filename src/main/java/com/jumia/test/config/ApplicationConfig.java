/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.cache.CacheBuilder;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;
import reactor.netty.tcp.TcpClient;
import static springfox.documentation.builders.PathSelectors.any;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Configuration
@EnableCircuitBreaker
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableSwagger2
public class ApplicationConfig {
    
    @Autowired
    private ApplicationProperties appProperties;
    
    /**
     * Caching Configuration
     * @return 
     */
    @Bean
    public CacheManager cacheManager() { // We leveraged on the use of in-memory to prevent frequent calls to the binlist endpoint
        GuavaCacheManager mgr = new GuavaCacheManager();
        mgr.setCacheBuilder( CacheBuilder.newBuilder().expireAfterAccess(appProperties.getExpiryTime(), TimeUnit.HOURS).maximumSize(1_000_000));
        mgr.setCacheNames(Arrays.asList("externalServiceData"));
        return mgr;
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    @Bean
    public ObjectWriter objectWriter(ObjectMapper objectMapper) {
        return objectMapper.writerWithDefaultPrettyPrinter();
    }

    @Bean
    public WebClient webClient() {
          SslProvider sslProvider = SslProvider.builder().sslContext(  
                                        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                                      ).defaultConfiguration(SslProvider.DefaultConfigurationType.NONE).build();

          TcpClient tcpClient = TcpClient.create().secure(sslProvider);  
          HttpClient httpClient = HttpClient.from(tcpClient);  
          ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);
          return WebClient.builder().clientConnector(httpConnector).build();
    }
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(appProperties.getSwaggerBasePackage()))
                .paths(any()).build().apiInfo(new ApiInfo(appProperties.getSwaggerDocTitle(), appProperties.getSwaggerDocDescription(), 
                        appProperties.getSwaggerDocVersion(), null, new Contact(appProperties.getDeveloperName(), appProperties.getDeveloperUrl(), appProperties.getDeveloperEmail()), 
                        null, null));
    }
}
