/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.service;

import com.jumia.test.exception.UnableToPersistDataException;
import com.jumia.test.models.CardQueryHit;
import com.jumia.test.models.User;
import java.util.List;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface CardQueryHitService {
    /**
     * Returns all the hits made by a user
     * @param user
     * @param pageRequest
     * @return 
     */
    List<CardQueryHit> getHitsByUser(User user, PageRequest pageRequest);
    /**
     * Gets the hits using a user ID and card number
     * @param user
     * @param cardNumber
     * @return the card query hit
     */
    CardQueryHit getHitByUserAndCardNumber(User user, String cardNumber);
    
    /**
     * Saves the number hits made for a card number per user
     * @param cardNumber
     * @return
     * @throws UnableToPersistDataException 
     */
    CardQueryHit saveCardQueryHit(String cardNumber, String username) throws UnableToPersistDataException;
}
