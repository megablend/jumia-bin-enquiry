/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.service.impl;

import com.jumia.test.dto.SearchCriteria;
import com.jumia.test.models.User;
import com.jumia.test.repos.UserRepo;
import com.jumia.test.service.QuerySpecification;
import com.jumia.test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    
    private final UserRepo userRepo;
    private final QuerySpecification querySpecification;
    
    public UserServiceImpl(UserRepo userRepo, QuerySpecification querySpecification) {
        this.userRepo = userRepo;
        this.querySpecification = querySpecification;
    }

    /** {@inheritDoc} */
    @Override
    public User getUserByEmail(String email) {
        Specification<User> spec = (Specification<User>) querySpecification.buildQuery(new SearchCriteria("email", ":", email));
        return userRepo.findOne(spec).orElseGet(() -> null);
    }

    /** {@inheritDoc} */
    @Override
    public User getLoggedInUser() {
        UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return getUserByEmail(userDetails.getName());
    }
    
}
