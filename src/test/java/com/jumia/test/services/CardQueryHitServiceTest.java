/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.services;

import com.jumia.test.exception.UnableToPersistDataException;
import com.jumia.test.models.CardQueryHit;
import com.jumia.test.models.User;
import com.jumia.test.repos.CardQueryHitRepo;
import com.jumia.test.service.CardQueryHitService;
import com.jumia.test.service.QuerySpecification;
import com.jumia.test.service.UserService;
import com.jumia.test.service.impl.CardQueryHitServiceImpl;
import com.jumia.test.utils.Util;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public class CardQueryHitServiceTest {
    
    private CardQueryHitRepo cardQueryHitRepo;
    private QuerySpecification querySpecification;
    private UserService userService;
    private CardQueryHitService cardQueryHitService;
    
    @Before
    public void setup() {
        cardQueryHitRepo = mock(CardQueryHitRepo.class);
        querySpecification = mock(QuerySpecification.class);
        userService = mock(UserService.class);
        cardQueryHitService = new CardQueryHitServiceImpl(cardQueryHitRepo, querySpecification, userService);
    }
    
    /**
     * Test case for GetHitsByUser
     * @throws Exception 
     */
    @Test
    public void testGetHitsByUser() throws Exception {
        
        // mock dependencies
        when((Specification<CardQueryHit>) querySpecification.buildQuery(any())).thenReturn(Util.stubSpecification());
        when(cardQueryHitRepo.findAll(isA(Specification.class), isA(Pageable.class))).thenReturn(Util.stubPage());
        
        // initiate service call
        List<CardQueryHit> queryResult = cardQueryHitService.getHitsByUser(new User(), new PageRequest(0, 3));
        
        assertNotNull(queryResult);
        assertFalse(queryResult.isEmpty());
        assertThat(queryResult, containsInAnyOrder( // confirm that each of the elements returned the appropriate expected attributes
                hasProperty("user", hasProperty("email", is("noniboycharsy@gmail.com"))),
                hasProperty("user", hasProperty("email", is("noniboycharsy1111@gmail.com")))
                ));
        assertThat(queryResult.size(), is(Util.getMockedQeryHits().size())); // confirm that the sizes are the same
    }
    
    /**
     * Test case for GetHitsByUserAndCardNumber
     * @throws Exception 
     */
    @Test
    public void testGetHitByUserAndCardNumber() throws Exception {
        // mock dependencies
        when((Specification<CardQueryHit>) querySpecification.buildQuery(any())).thenReturn(Util.stubSpecification());
        when(cardQueryHitRepo.findOne(isA(Specification.class))).thenReturn(Optional.of(Util.mockCardQueryHit()));
        
        // make the service call
        CardQueryHit cardQueryHit = cardQueryHitService.getHitByUserAndCardNumber(new User(), "zzzz");
        
        assertNotNull(cardQueryHit);
        assertEquals("noniboycharsy@gmail.com", cardQueryHit.getUser().getEmail());
        assertEquals("435333", cardQueryHit.getCardNumber());
        assertThat(cardQueryHit.getHits(), is(2));
    }
    
    /**
     * Test case for SaveCardQueryHit
     * @throws Exception 
     */
    @Test
    public void testSaveCardQueryHit() throws Exception {
        
        when(userService.getUserByEmail(any())).thenReturn(new User());
        when((Specification<CardQueryHit>) querySpecification.buildQuery(any())).thenReturn(Util.stubSpecification());
        when(cardQueryHitRepo.findOne(isA(Specification.class))).thenReturn(Optional.of(Util.mockCardQueryHit()));
        when(cardQueryHitRepo.save(any())).thenReturn(new CardQueryHit());
        
        // make service call
        CardQueryHit cardQueryHit = cardQueryHitService.saveCardQueryHit("zzzz", "ssss");
        assertNotNull(cardQueryHit);
    }
    
    @Test(expected = UnableToPersistDataException.class)
    public void testFailedSaveCardQueryHit() throws Exception {
        when(userService.getUserByEmail(any())).thenReturn(new User());
        when((Specification<CardQueryHit>) querySpecification.buildQuery(any())).thenReturn(Util.stubSpecification());
        when(cardQueryHitRepo.findOne(isA(Specification.class))).thenReturn(Optional.of(Util.mockCardQueryHit()));
        when(cardQueryHitRepo.save(any())).thenReturn(null);
        
        // make service call
        cardQueryHitService.saveCardQueryHit("zzzz", "ssss");
    }
    
    @After
    public void tearDown() {
        reset(querySpecification);
    }
}
