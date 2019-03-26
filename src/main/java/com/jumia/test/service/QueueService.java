/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.service;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface QueueService {
    /**
     * This method sends payload to a topic
     * @param topic
     * @param payload 
     */
    void sendToTopic(String topic, String payload);
    /**
     * This method handles the registering of users card hit requests
     * @param payload 
     */
    void registerCardHits(String payload);
}
