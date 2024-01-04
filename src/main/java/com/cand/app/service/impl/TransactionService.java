package com.cand.app.service.impl;

import com.cand.app.entity.Customer;
import com.cand.app.entity.UniqueTransaction;
import com.cand.app.exception.CustomerException;
import com.cand.app.exception.Message;
import com.cand.app.repository.IBankTransaction;
import com.cand.app.service.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class TransactionService implements ITransactionService {

    @Autowired
    private IBankTransaction trans;

    @Override
    public void save(UniqueTransaction uniqueTransaction) {
        trans.save(uniqueTransaction);
    }

    @Override
    public void saveAll(Set<UniqueTransaction> transactions) {
        trans.saveAll(transactions);
    }

    @Override
    public Set<UniqueTransaction> getAll() {
        Set<UniqueTransaction> x = new HashSet<>();
        trans.findAll().forEach(e -> x.add(e));
        return x;
    }

    @Override
    public Set<UniqueTransaction> getCustomerTransaction(String customer) {
        Set<UniqueTransaction> oneTransactions = new HashSet<>(trans.findByCustomerName(customer));
        return Optional.ofNullable(oneTransactions).orElseThrow(() -> new CustomerException(Message.CUSTOMER_NOT_FOUND));
    }

    @Override
    public Set<UniqueTransaction> getAllCustomersTransaction(List<Customer> customers) {
        Set<UniqueTransaction> t = new HashSet<>();
        customers.stream().forEach(e -> trans.findByCustomerName(e.getFullName()));
        return t;
    }

    @Override
    public Set<UniqueTransaction> getUnknownCustomerTransaction() {
        return getCustomerTransaction(Customer.UNKNOWN);
    }

    @Override
    public Set<UniqueTransaction> getAllKnownCustomerTransaction() {
        return trans.findAllKnownTransactionsFor(Customer.UNKNOWN);
    }

}
