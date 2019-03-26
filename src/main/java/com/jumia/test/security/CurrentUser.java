/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@AuthenticationPrincipal
@Documented
public @interface CurrentUser {
    
}
