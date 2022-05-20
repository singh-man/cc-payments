package com.cand.app.dto;

import com.cand.app.entity.Bank;
import com.cand.app.entity.Customer;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is the transfer Object via REST API
 * @Author Manish Singh
 */
public class CustomerDTO {

    private Long id;
    private String name;
    private Map<String, String> banks;

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getFullName();
        this.banks = customer.getAccounts().stream().collect(Collectors.toMap(Bank::getRoutingNumber, Bank::getAccountNumber));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getBanks() {
        return banks;
    }
}
