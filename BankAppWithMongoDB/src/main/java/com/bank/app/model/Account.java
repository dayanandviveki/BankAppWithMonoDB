package com.bank.app.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Entity
@Document(collection = "Account")
public class Account {

    @Id
    private Integer accountNo;

    private String userName;

    @Enumerated(EnumType.STRING)
    private StatusEnum accountType;

    private String branchName;

    private String IFSC;
    
    private Float accBalance;
    
    private boolean isActive;

    public enum StatusEnum {
        savings, current
    }
}