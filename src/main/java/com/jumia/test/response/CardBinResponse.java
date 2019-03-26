/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.response;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public class CardBinResponse extends Response {
    private final boolean success;
    private final Object payload;
    
    public CardBinResponse(boolean success, Object payload) {
        this.success = success;
        this.payload = payload;
    }

    @Override
    public boolean getSuccess() {
        return success;
    }

    @Override
    public Object getPayload() {
        return payload;
    }
}
