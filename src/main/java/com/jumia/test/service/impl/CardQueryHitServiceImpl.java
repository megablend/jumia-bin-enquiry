/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.service.impl;

import com.jumia.test.dto.SearchCriteria;
import com.jumia.test.exception.UnableToPersistDataException;
import com.jumia.test.models.CardQueryHit;
import com.jumia.test.models.User;
import com.jumia.test.repos.CardQueryHitRepo;
import com.jumia.test.service.CardQueryHitService;
import com.jumia.test.service.QuerySpecification;
import com.jumia.test.service.UserService;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Service
@Transactional
@Slf4j
public class CardQueryHitServiceImpl implements CardQueryHitService {
    
    private final CardQueryHitRepo cardQueryHitRepo;
    private final QuerySpecification querySpecification;
    private final UserService userService;
    
    @Autowired
    public CardQueryHitServiceImpl(CardQueryHitRepo cardQueryHitRepo, QuerySpecification querySpecification, UserService userService) {
        this.cardQueryHitRepo = cardQueryHitRepo;
        this.querySpecification = querySpecification;
        this.userService = userService;
    }

    /** {@inheritDoc} */
    @Override
    public List<CardQueryHit> getHitsByUser(User user, PageRequest pageRequest) {
        Specification<CardQueryHit> spec = (Specification<CardQueryHit>) querySpecification.buildQuery(new SearchCriteria("user", ":", user));
        Page<CardQueryHit> result = cardQueryHitRepo.findAll(spec, pageRequest);
//        log.info("The page request is {}", result.getContent());
        return null == result ? Collections.emptyList() : result.getContent();
    }

    /** {@inheritDoc} */
    @Override
    public CardQueryHit getHitByUserAndCardNumber(User user, String cardNumber) {
        Specification<CardQueryHit> spec = (Specification<CardQueryHit>) querySpecification.buildQuery(new SearchCriteria("user", ":", user));
        Specification<CardQueryHit> spec2 = (Specification<CardQueryHit>) querySpecification.buildQuery(new SearchCriteria("cardNumber", ":", cardNumber));
        return cardQueryHitRepo.findOne(Specification.where(spec).and(spec2)).orElseGet(() -> null);
    }

    /** {@inheritDoc} */
    @Override
    public CardQueryHit saveCardQueryHit(String cardNumber, String username) throws UnableToPersistDataException {
        
        User user = userService.getUserByEmail(username);
        CardQueryHit cardQueryHit = getHitByUserAndCardNumber(user, cardNumber);
        
        if (null == cardQueryHit) 
            cardQueryHit = new CardQueryHit(user, cardNumber, 1);
        else 
            cardQueryHit.setHits(cardQueryHit.getHits() + 1);
        
        CardQueryHit result = cardQueryHitRepo.save(cardQueryHit);
        if (null == result)
            throw new UnableToPersistDataException("Unable to persist card query hit record: ", CardQueryHit.class.getName());
        return result;
    }
    
}
