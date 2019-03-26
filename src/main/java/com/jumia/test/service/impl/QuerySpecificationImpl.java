/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.service.impl;

import com.jumia.test.dto.SearchCriteria;
import com.jumia.test.dto.SearchCriteriaOperation;
import com.jumia.test.models.User;
import com.jumia.test.service.QuerySpecification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Service
public class QuerySpecificationImpl implements QuerySpecification {

    /** {@inheritDoc} */
    @Override
    public Specification<? extends Object> buildQuery(SearchCriteria criteria) {
        return (Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            if (criteria.getOperation().equals(SearchCriteriaOperation.EQUAL_OPERATION))
                return cb.equal(root.get(criteria.getKey()), criteria.getValue());
            return null;
        };
    }
    
}
