package com.cand.app.service.impl;

import com.cand.app.entity.Customer;
import com.cand.app.exception.CustomerException;
import com.cand.app.exception.Message;
import com.cand.app.repository.ICustomer;
import com.cand.app.service.ICustomerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CustomerService implements ICustomerService {

    private ICustomer customerRep;

    @Override
    public Set<String> getAllCustomerNames() {
        return new HashSet<>(customerRep.findAllCustomerNames());
    }

    @Override
    public Customer getCustomerAccountDetails(String name) {
        log.info("Getting customer account details : " + name);
        return Optional.ofNullable(customerRep.findByFullName(name)).orElseThrow(() -> new CustomerException(Message.CUSTOMER_NOT_FOUND));
    }

    @Override
    public void save(Customer customer) {
        customerRep.save(customer);
    }

    @Override
    public void saveAll(Set<Customer> customers) {
        customerRep.saveAll(customers);
    }

    @Override
    public Set<Customer> getAll() {
        Set<Customer> cus = new HashSet<>();
        customerRep.findAll().forEach(e -> cus.add(e));
        return cus;
    }

    @Autowired
    public void setCustomerRep(ICustomer customerRep) {
        this.customerRep = customerRep;
    }
}
