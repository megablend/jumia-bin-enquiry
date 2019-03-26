/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.services;

import com.jumia.test.models.User;
import com.jumia.test.repos.UserRepo;
import com.jumia.test.service.QuerySpecification;
import com.jumia.test.service.UserService;
import com.jumia.test.service.impl.UserServiceImpl;
import com.jumia.test.utils.Util;
import java.util.Optional;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public class UserServiceTest {
    
    private UserRepo userRepo;
    private QuerySpecification querySpecification;
    private UserService userService;
    
    @Before
    public void setup() {
        userRepo = mock(UserRepo.class);
        querySpecification = mock(QuerySpecification.class);
        userService = new UserServiceImpl(userRepo, querySpecification);
    }
    
    @Test
    public void testGetUserByEmail() throws Exception {
        when((Specification<User>) querySpecification.buildQuery(any())).thenReturn(Util.stubUserSpecification());
        when(userRepo.findOne(isA(Specification.class))).thenReturn(Optional.of(new User()));
        
        // make service call
        User user = userService.getUserByEmail("zzz");
        assertNotNull(user);
    }
}
