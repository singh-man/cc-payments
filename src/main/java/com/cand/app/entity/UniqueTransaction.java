package com.cand.app.entity;

import com.cand.app.json.JsonTransaction;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
public class UniqueTransaction {

    @Id
    private String transactionId;
    private String routingNumber;
    private String accountNumber;
    private BigDecimal amount;
    private String customerName;

    public void populateUniqueTransaction(JsonTransaction.Transaction transaction) {
        this.transactionId = transaction.id;
        this.accountNumber = transaction.toAccount.account_number;
        this.routingNumber = transaction.toAccount.routing_number;
        this.amount = new BigDecimal(transaction.amount.amount);
    }

}