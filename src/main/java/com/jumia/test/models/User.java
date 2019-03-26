/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jumia.test.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", indexes = {
    @Index(columnList = "email", name = "users_email_index")
})
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Email
    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "password", length = 100)
    private String password;
    
    @Column(name = "userRole")
    private String userRole;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
    
    @PrePersist
    public void updateDate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
