/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.service;

import com.jumia.test.models.User;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface UserService {
    /**
     * Gets the details of a user based on selected email address
     * @param email
     * @return 
     */
    User getUserByEmail(String email);
    /**
     * Returns the details of the user that is logged in
     * @return 
     */
    User getLoggedInUser();
}
