/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.repos;

import com.jumia.test.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
}
