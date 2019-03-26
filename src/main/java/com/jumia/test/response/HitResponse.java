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
public class HitResponse extends Response {
    
    private final boolean success;
    private final int start;
    private final int limit;
    private final int size;
    private final Object payload;

    public int getStart() {
        return start;
    }

    public int getLimit() {
        return limit;
    }

    public int getSize() {
        return size;
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
