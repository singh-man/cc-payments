package com.cand.app.service;

import com.cand.app.entity.Customer;
import com.cand.app.exception.CustomerException;
import com.cand.app.exception.Message;
import com.cand.app.repository.ICustomer;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerService implements ICustomerService {

    private static final Logger log = Logger.getLogger(CustomerService.class);

    private ICustomer customerRep;

    @Override
    public Set<String> getAllCustomerNames() {
        return new HashSet<>(customerRep.findAllCustomerNames());
    }

    @Override
    public Customer getCustomerAccountDetails(String name) {
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
