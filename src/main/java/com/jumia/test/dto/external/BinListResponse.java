/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BinListResponse implements ExternalServiceResponse {
    private CardNumber number;
    private String scheme;
    private String type;
    private String brand;
    private boolean prepaid;
    private Country country;
    private Bank bank;
}
