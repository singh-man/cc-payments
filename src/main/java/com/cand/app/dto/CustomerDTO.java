package com.cand.app.dto;

import com.cand.app.entity.Customer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the transfer Object via REST API
 *
 * @Author Manish Singh
 */
public record CustomerDTO(Long id, String name, List<Account> banks) {

    public static CustomerDTO getCustomerDTO(Customer customer) {
        return new CustomerDTO(customer.getId(),
        customer.getFullName(),
        customer.getAccounts().stream()
                .map(e -> new Account(e.getRoutingNumber(), e.getAccountNumber(), e.getBalance()))
                .collect(Collectors.toList()));
    }
}
