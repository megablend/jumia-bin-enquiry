/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.controllers;

import com.jumia.test.config.ApplicationProperties;
import com.jumia.test.filters.JwtTokenAuthenticationFilter;
import com.jumia.test.models.User;
import com.jumia.test.service.CardQueryHitService;
import com.jumia.test.service.ExternalService;
import com.jumia.test.service.QueueService;
import com.jumia.test.service.UserService;
import com.jumia.test.utils.Util;
import org.hamcrest.Matchers;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CardDetailsController.class)
public class CardDetailsControllerTest {
    
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext wac; 
    @MockBean
    private ExternalService externalService;
    @MockBean
    private QueueService queueService;
    @MockBean
    private ApplicationProperties properties;
    @MockBean
    private CardQueryHitService cardQueryHitService;
    @MockBean
    private UserService userService;
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String HEADER_PREFIX = "Bearer ";
    private static final String USERNAME = "noniboycharsy@gmail.com";
    private String jwtToken;
    
    @Before
    public void setup() {
        properties = new ApplicationProperties();
        properties.setSecurityJwtHeader(AUTHORIZATION_HEADER);
        properties.setSecurityJwtPrefix(HEADER_PREFIX);
        properties.setAuthPath("/auth");
        
        mvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(new JwtTokenAuthenticationFilter(properties))
                                                     .build();
        jwtToken = Util.generateToken(USERNAME, properties);
    }
    
    /**
     * Test case for query by BIN
     * @throws Exception 
     */
    @Test
    @Ignore
//    @WithMockUser(username = "noniboycharsy@gmail.com", roles={"USER"})
    public void test_validate_card_bin_details() throws Exception {
        /**
         * {
                "success": true,
                "payload": {
                    "scheme": "mastercard",
                    "type": "debit",
                    "bank": "GUARANTY TRUST BANK"
                }
           }
         */
        String json = "{ \"success\": true, \"payload\": { \"scheme\": \"mastercard\", \"type\": \"debit\", \"bank\": \"GUARANTY TRUST BANK\" } }";
        
        // mock services
        when(userService.getLoggedInUser()).thenReturn(new User());
        when(externalService.getCardDetails(any())).thenReturn(Util.getSuccessMockBinResponse());
        doNothing().when(queueService).sendToTopic(any(), any());
        
        MvcResult result = mvc.perform(get("/card-scheme/verify/539983").header(AUTHORIZATION_HEADER, HEADER_PREFIX + jwtToken)
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.success", Matchers.is(true)))
                            .andExpect(jsonPath("$.payload", Matchers.notNullValue())).andReturn();
        
        verify(externalService, times(1)).getCardDetails(any());
        verify(userService, times(1)).getLoggedInUser();
        verify(queueService, times(1)).sendToTopic(any(), any());
        assertEquals(result.getResponse().getContentAsString(), json);
    }
    
    /**
     * Test case for query by stats
     * @throws Exception 
     */
    @Test
    @Ignore
//    @WithMockUser(username = "noniboycharsy@gmail.com", roles={"USER"})
    public void test_validate_card_query_hits() throws Exception {
        /**
         * {
                "success": true,
                "start": 0,
                "limit": 3,
                "size": 2,
                "payload": {
                    "435333": "2",
                    "539983": "10"
                }
            }
         */
        String json = "{ \"success\": true, \"start\": 0, \"limit\": 3, \"size\": 2, \"payload\": { \"435333\": \"2\", \"539983\": \"10\" } }";
        
        // mock services
        when(userService.getLoggedInUser()).thenReturn(new User());
        when(cardQueryHitService.getHitsByUser(any(), any())).thenReturn(Util.getMockedQeryHits());
        
        MvcResult result = mvc.perform(get("/card-scheme/stats?start=0&limit=3").header(AUTHORIZATION_HEADER, HEADER_PREFIX + jwtToken)
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.success", Matchers.is(true)))
                            .andExpect(jsonPath("$.start", Matchers.is(0)))
                            .andExpect(jsonPath("$.limit", Matchers.is(3)))
                            .andExpect(jsonPath("$.size", Matchers.is(2)))
                            .andExpect(jsonPath("$.payload", Matchers.notNullValue())).andReturn();
        
        verify(userService, times(1)).getLoggedInUser();
        verify(cardQueryHitService, times(1)).getHitsByUser(any(), any());
        assertEquals(result.getResponse().getContentAsString(), json);
    }
    
    /**
     * Test case for invalid card number
     * @throws Exception 
     */
    @Test
    @Ignore
    public void whenCardNumberIsInvalid_thenReturnsStatus400() throws Exception {
        mvc.perform(get("/card-scheme/verify/539"))
                .andExpect(status().isBadRequest());
    }
    
    @After
    public void tearDown() {
        reset(userService);
        reset(externalService);
        reset(externalService);
    }
}
