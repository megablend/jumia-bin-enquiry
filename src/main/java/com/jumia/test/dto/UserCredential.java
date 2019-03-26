/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author cmegafu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCredential {
    private String username;
    private String password;
}
