/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.service;

import com.jumia.test.dto.external.BinListResponse;
import com.jumia.test.dto.external.ExternalServiceResponse;
import java.util.function.Supplier;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface ExternalService {
    /**
     * Get the details of a given
     * @param cardNumber
     * @return the card details response object
     */
    ExternalServiceResponse getCardDetails(Supplier<String> cardNumber);
    /**
     * The default response when there is an issue with the external service
     * @return 
     */
    ExternalServiceResponse defaultResponse(Supplier<String> cardNumber);
}
