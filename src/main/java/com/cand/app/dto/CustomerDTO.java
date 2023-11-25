package com.cand.app.dto;

import com.cand.app.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the transfer Object via REST API
 *
 * @Author Manish Singh
 */
@Getter
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    private String name;
    private List<Account> banks;

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getFullName();

        this.banks = customer.getAccounts().stream()
                .map(e -> new Account(e.getRoutingNumber(), e.getAccountNumber(), e.getBalance()))
                .collect(Collectors.toList());
    }
}
