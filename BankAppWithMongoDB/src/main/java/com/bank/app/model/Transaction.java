package com.bank.app.model;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection  = "Transation")
public class Transaction {

	@Id
    private Integer transactionId;

    @Enumerated(EnumType.STRING)
    private StatusEnum transactionType;

    private Integer accountNo;

    private String userName;

    private Float transactionAmount;

    private Integer beneficiaryAccountNo;

    private Date trtzDate;

    private Float closingBalance;

    public enum StatusEnum {
        debit, credit, transfer;
    }
}
