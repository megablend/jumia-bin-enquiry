/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.security;

import com.jumia.test.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author cmegafu
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.jumia.test.models.User user = userService.getUserByEmail(username);
        if (null == user) 
            throw new UsernameNotFoundException("The user with the email address " + username + " was not found");
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
		                	.commaSeparatedStringToAuthorityList("ROLE_" + user.getUserRole().toUpperCase());
        return new User(user.getEmail(), user.getPassword(), true, true, true, true, grantedAuthorities);
    }
}
