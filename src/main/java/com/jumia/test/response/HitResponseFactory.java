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
public class HitResponseFactory implements ResponseAbstractFactory {
    
    private final boolean success;
    private final int start;
    private final int limit;
    private final int size;
    private final Object payload;

    @Override
    public Response createResponse() {
        return new HitResponse(success, start, limit, size, payload);
    }
    
}
