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
public abstract class Response {
    /**
     * Determines whether a response is successful
     * @return boolean
     */
    public abstract boolean getSuccess();
    
    /**
     * Gets the payload of the response
     * @return 
     */
    public abstract Object getPayload();
}
