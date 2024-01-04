package com.cand.app.entity;

import com.cand.app.exception.CustomerException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Customer {

    public static final String UNKNOWN = "UNKNOWN";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private String fullName;

    @OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "customer")
    @JsonProperty("banks")
    private Set<Bank> accounts;

    public Customer(String fullName, Bank... banks) {
        this.fullName = fullName;
        this.accounts = new HashSet<>(Arrays.asList(banks));
    }

    private Bank getBank(String accountNumber, String routingNumber) {
        for (Bank b : accounts) {
            if (b.getRoutingNumber().equals(routingNumber) && b.getAccountNumber().equals(accountNumber)) return b;
        }
        return null;
    }

    public boolean isMyAccount(String accountNumber, String routingNumber) {
        Bank bank = getBank(accountNumber, routingNumber);
        return bank != null ? true : false;
    }

    public void addAmount(String accountNumber, String routingNumber, BigDecimal amount) {
        Bank bank = getBank(accountNumber, routingNumber);
        if (bank != null)
            bank.setBalance(bank.getBalance().add(amount));
        else
            throw new CustomerException(String.format("Bank account $s not found for User %s Can't add the requested amount %s",
                    accountNumber, getFullName(), amount.toString()));

    }

}
