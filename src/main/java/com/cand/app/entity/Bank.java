package com.cand.app.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor()
public class Bank {

    @Id
    @GeneratedValue
    private long id;

    @NonNull
    private String routingNumber;
    @NonNull
    private String accountNumber;

    private BigDecimal balance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cust_id") // owns the relationship
    @NonNull
    private Customer customer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return routingNumber.equals(bank.routingNumber) && accountNumber.equals(bank.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routingNumber, accountNumber);
    }
}
