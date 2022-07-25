package com.cand.app.entity;

import com.cand.app.json.JsonTransaction;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
@Data
public class UniqueTransaction {

    @Id
    private String transactionId;
    private String routingNumber;
    private String accountNumber;
    private BigDecimal amount;
    private String customerName;

    public void populateDAO(JsonTransaction.Transaction transaction) {
        transactionId = transaction.id;
        routingNumber = transaction.myto.routing_number;
        accountNumber = transaction.myto.account_number;
        amount = new BigDecimal(transaction.amount.amount);
    }

}