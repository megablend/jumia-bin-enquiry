/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.controllers;

import com.jumia.test.config.ApplicationProperties;
import com.jumia.test.dto.CardHitResponse;
import com.jumia.test.dto.CardResponse;
import com.jumia.test.dto.external.BinListResponse;
import com.jumia.test.dto.external.DefaultExternalServiceResponse;
import com.jumia.test.dto.external.ExternalServiceResponse;
import com.jumia.test.exception.GatewayNotResponsiveException;
import com.jumia.test.models.CardQueryHit;
import com.jumia.test.models.User;
import com.jumia.test.response.CardBinResponseFactory;
import com.jumia.test.response.HitResponseFactory;
import com.jumia.test.response.ResponseFactory;
import com.jumia.test.service.CardQueryHitService;
import com.jumia.test.service.ExternalService;
import com.jumia.test.service.QueueService;
import com.jumia.test.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.groupingBy;
import javax.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.lang.StringUtils.substring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@RestController
@RequestMapping("/card-scheme")
@Slf4j
@Validated
@Api(value = "CardDetailsController", description = "To Manage the retrieval of card BIN details and users query hits", tags = {"Card Details Request"})
public class CardDetailsController {
    
    @Autowired
    private ExternalService externalService;
    @Autowired
    private QueueService queueService;
    @Autowired
    private ApplicationProperties properties;
    @Autowired
    private CardQueryHitService cardQueryHitService;
    @Autowired
    private UserService userService;
    
    /**
     * Gets card details based on card number
     * @param cardNumber
     * @return
     * @throws GatewayNotResponsiveException 
     */
    @GetMapping("/verify/{cardNumber}")
    @ApiOperation(value = "Get Card BIN Details", notes = "This endpoint enables customers to retrieve BIN details of their cards", nickname = "Verify Card BIN Details")
    public ResponseEntity getCardDetails(@PathVariable @Pattern(regexp = "^\\d{6,}$", message = "Invalid card number provided, must not be less than six (6) digits") String cardNumber) throws GatewayNotResponsiveException {
        String formattedCardNumber = substring(cardNumber, 0, 6);
        ExternalServiceResponse response = externalService.getCardDetails(() -> formattedCardNumber);
        
        if (response instanceof DefaultExternalServiceResponse)
            throw new GatewayNotResponsiveException(((DefaultExternalServiceResponse) response).getMessage());
        
        BinListResponse res = (BinListResponse) response;
        if (null == res)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.createResponse(new CardBinResponseFactory(false, null)));
        else // save card details hit
            queueService.sendToTopic(properties.getActivemqCardDetailsHitsTopic(), new StringBuilder(formattedCardNumber).append(":").append(userService.getLoggedInUser().getEmail()).toString());
        return ResponseEntity.ok(ResponseFactory.createResponse(new CardBinResponseFactory(true, new CardResponse(res.getScheme(), res.getType(), null != res.getBank() ? res.getBank().getName() : null))));
    }
    
    /**
     * Gets the statistics of a user's hits
     * @param start
     * @param limit
     * @return 
     */
    @GetMapping("/stats")
    @ApiOperation(value = "Get Card Query Hits", notes = "This endpoint enables customers to know the number of hits they have recorded for card BIN queries", nickname = "Get Card Query Hits")
    public ResponseEntity getUserHitsStatistics(@RequestParam int start, @RequestParam int limit) {
        User user = userService.getLoggedInUser();
        
        if (null == user) // check to see if the details of this exists
            throw new UsernameNotFoundException("Unable to fetch the details of this user");
        
        List<CardQueryHit> result = cardQueryHitService.getHitsByUser(user, new PageRequest(start, limit));
        JSONObject obj = new JSONObject();
        boolean success = false;
        
        if (!result.isEmpty()) {
            success = true;
            Map<String, List<CardQueryHit>> groupedResult = result.parallelStream().collect(groupingBy(CardQueryHit::getCardNumber));
            groupedResult.keySet().parallelStream().forEach((k) -> {
                int sumOfHits = groupedResult.get(k).stream().mapToInt(CardQueryHit::getHits).sum();
                obj.put(k, String.valueOf(sumOfHits));
            });
        }
        
        return ResponseEntity.ok(ResponseFactory.createResponse(new HitResponseFactory(success, start, limit, result.size(), obj)));
    }
}
