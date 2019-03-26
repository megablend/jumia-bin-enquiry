/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.services;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;
import reactor.netty.tcp.TcpClient;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class ExternalServiceTest {
    
    @Autowired
    private WebTestClient webClient;
    
    @Before
    public void setup() {
//        response = "{ \"number\": {}, \"scheme\": \"mastercard\", \"type\": \"debit\", \"brand\": \"Debit\", \"country\": { \"numeric\": \"566\", \"alpha2\": \"NG\", \"name\": \"Nigeria\", \"emoji\": \"🇳🇬\", \"currency\": \"NGN\", \"latitude\": 10, \"longitude\": 8 }, \"bank\": { \"name\": \"GUARANTY TRUST BANK\", \"phone\": \"234-1-4480000 OR 080 29002900 OR 080 390039\" } }";
        SslProvider sslProvider = SslProvider.builder().sslContext(  
                                        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                                      ).defaultConfiguration(SslProvider.DefaultConfigurationType.NONE).build();

        TcpClient tcpClient = TcpClient.create().secure(sslProvider);  
        HttpClient httpClient = HttpClient.from(tcpClient);  
        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);
        webClient =  WebTestClient.bindToServer(httpConnector).build();
    }
    
    /**
     * Test case to test connection to binlist
     * @throws Exception 
     */
    @Test
    public void testGetCardDetails() throws Exception {
        webClient.get().uri("https://lookup.binlist.net/53998316").accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                        .expectBody()
                        .jsonPath("$.scheme").isEqualTo("mastercard")
                        .jsonPath("$.type").isEqualTo("debit")
                        .jsonPath("$.brand").isEqualTo("Debit")
                        .jsonPath("$.country").isNotEmpty()
                        .jsonPath("$.bank").isNotEmpty();
    }
}
