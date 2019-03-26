/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Entity
@Table(name = "card_query_hits", indexes = {
    @Index(columnList = "user_id,card_number", name = "user_id_card_number_index"),
    @Index(columnList = "user_id", name = "user_id_index"),
    @Index(columnList = "card_number", name = "card_number_index")
}, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "card_number"}, name = "user_id_card_number_unique_index")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardQueryHit implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    @Column(name = "card_number", length = 6)
    private String cardNumber;
    
    @Column(name = "hits")
    private int hits;
    
    public CardQueryHit(User user, String cardNumber, int hits) {
        this.user = user;
        this.cardNumber = cardNumber;
        this.hits = hits;
    }
}
