package com.cand.app.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bank")
public class Bank {

    @Id
    @GeneratedValue
    private long id;

    private String routingNumber;
    private String accountNumber;

    private BigDecimal balance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="cust_id") // owns the relationship
    private Customer customer;

    public Bank() {
    }

    public Bank(String routingNumber, String accountNumber, Customer customer) {
        this.routingNumber = routingNumber;
        this.accountNumber = accountNumber;
        this.customer = customer;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Objects.equals(routingNumber, bank.routingNumber) && Objects.equals(accountNumber, bank.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routingNumber, accountNumber);
    }

}
