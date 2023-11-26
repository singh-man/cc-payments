package com.cand.app.service.impl;

import com.cand.app.entity.Bank;
import com.cand.app.entity.Customer;
import com.cand.app.exception.CustomerException;
import com.cand.app.exception.Message;
import com.cand.app.repository.ICustomer;
import com.cand.app.service.ICustomerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    public Customer getCustomerAccountDetails(String name) throws CustomerException {
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

    @Override
    @Async
    /*
     * resolves org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role
     * Customer to Bank is OneToMany and Bank is lazy loaded by default
     */
    @Transactional(value = Transactional.TxType.REQUIRED, dontRollbackOn = Exception.class)
    public Boolean addXAmountToCustomerAfterYTime(String name, float amt, int time) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(time));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Customer customerAccountDetails = getCustomerAccountDetails(name);
        if (customerAccountDetails != null) {
            // find bank with highest balance or use the first available bank
            Bank bank = customerAccountDetails.getAccounts().stream()
                    .min((o1, o2) -> o1.getBalance().compareTo(o1.getBalance()))
                    .orElse(customerAccountDetails.getAccounts().iterator().next());
            bank.setBalance(bank.getBalance().add(BigDecimal.valueOf(amt)));
            customerRep.save(customerAccountDetails);
            log.info(String.format("Added amount %f to customer's account with highest balance. %s", amt, name));
            return true;
        }
        log.info(String.format("Customer %s not found. No amount was added!!", name));
        return false;
    }

    @Autowired
    public void setCustomerRep(ICustomer customerRep) {
        this.customerRep = customerRep;
    }
}
