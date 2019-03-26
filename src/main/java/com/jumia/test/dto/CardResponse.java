/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
    private String scheme;
    private String type;
    private String bank;
}
