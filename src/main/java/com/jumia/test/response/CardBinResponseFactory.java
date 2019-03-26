/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.response;

import lombok.AllArgsConstructor;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@AllArgsConstructor
public class CardBinResponseFactory implements ResponseAbstractFactory {
    private final boolean success;
    private final Object payload;
    
    @Override
    public Response createResponse() {
        return new CardBinResponse(success, payload);
    }
    
}
