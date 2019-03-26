/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.service.impl;

import com.jumia.test.config.ApplicationProperties;
import com.jumia.test.dto.external.BinListResponse;
import com.jumia.test.dto.external.DefaultExternalServiceResponse;
import com.jumia.test.dto.external.ExternalServiceResponse;
import com.jumia.test.service.ExternalService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Service
@Slf4j
public class ExternalServiceImpl implements ExternalService {
    
    private final WebClient webClient;
    private final ApplicationProperties properties;
    
    @Autowired
    public ExternalServiceImpl(WebClient webClient, ApplicationProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    /** {@inheritDoc} */
    @Override
    @HystrixCommand(fallbackMethod = "defaultResponse", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")
      })
    @Cacheable(value = "externalServiceData", unless="#result == null") 
    public ExternalServiceResponse getCardDetails(Supplier<String> cardNumber) {
        String url = new StringBuilder(properties.getBinListUrl()).append(cardNumber.get()).toString();
//        log.info("The url is {}", url);
        Mono<BinListResponse> response = webClient.get()
                        .uri(url)
                        .accept(MediaType.APPLICATION_JSON).exchange()
                        .flatMap(clientResponse -> clientResponse.bodyToMono(BinListResponse.class));
        return response.block();
    }

    /** {@inheritDoc} */
    @Override
    public ExternalServiceResponse defaultResponse(Supplier<String> cardNumber) {
        return new DefaultExternalServiceResponse("Unable to retrieve BIN details from the gateway provider, please try again");
    }
    
}
