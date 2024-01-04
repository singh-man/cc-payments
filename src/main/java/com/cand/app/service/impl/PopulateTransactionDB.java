package com.cand.app.service.impl;

import com.cand.app.entity.Customer;
import com.cand.app.entity.UniqueTransaction;
import com.cand.app.exception.CustomerException;
import com.cand.app.json.JsonTransaction;
import com.cand.app.service.ICustomerService;
import com.cand.app.service.ITransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Setter
@Slf4j
class PopulateTransactionDB {

    @Autowired
    ICustomerService customerService;

    @Autowired
    ITransactionService transactionService;

//    public PopulateTransactionDB(FileProcessor fileProcessor, List<Customer> customers, List<Path> allFiles) {
//        this.fileProcessor = fileProcessor;
//        this.customers = customers;
//        this.allFiles = allFiles;
//    }

    public Set<UniqueTransaction> prepareBankTransactionFrom(Path p, List<Customer> customers) {
        JsonTransaction trans = null;
        try {
            trans = readFileAndPrepareObject(p, JsonTransaction.class);
        } catch (IOException e) {
            // Do not stop processing; continue!
            log.error("Failed to load files from given path : " + e.getLocalizedMessage());
        }
        Set<UniqueTransaction> transactDAO = new HashSet<>();
        for (JsonTransaction.Transaction t : trans.transactions) {
            Customer customer = customers.stream()
                    .filter(c -> c.isMyAccount(t.toAccount.account_number, t.toAccount.routing_number))
                    .findFirst()
                    .orElse(new Customer(Customer.UNKNOWN));
            UniqueTransaction uniqueTransaction = new UniqueTransaction();
            uniqueTransaction.populateUniqueTransaction(t);
            uniqueTransaction.setCustomerName(customer.getFullName());
            try {
                if(!customer.getFullName().equals(Customer.UNKNOWN))
                    customer.addAmount(uniqueTransaction.getAccountNumber(), uniqueTransaction.getRoutingNumber(), BigDecimal.valueOf(t.amount.amount));
            } catch (CustomerException e) {
                log.info(e.getLocalizedMessage());
            }
            transactDAO.add(uniqueTransaction);
        }
        customerService.saveAll(new HashSet<>(customers));
        transactionService.saveAll(transactDAO);
        return transactDAO;
    }

    private <T> T readFileAndPrepareObject(Path p, Class<T> glass) throws IOException {
        Reader reader = Files.newBufferedReader(p);
        return new ObjectMapper().readValue(reader, glass);
    }

    @Async
    public void process(List<Customer> customers, Set<UniqueTransaction> uniqueTransactions) {
        Set<UniqueTransaction> totalKnownCustomers = new HashSet<>();
        for (Customer c : customers) {
            Set<UniqueTransaction> knownCustomerTrans = transactionService.getCustomerTransaction(c.getFullName());
            totalKnownCustomers.addAll(knownCustomerTrans);
            BigDecimal sum = knownCustomerTrans.stream().map(e -> e.getAmount()).reduce(BigDecimal.valueOf(0), BigDecimal::add);
            System.out.println(String.format("Balance for %s: count=%d sum=%.2f USD", c.getFullName(),
                    knownCustomerTrans.size(), sum));
        }
        Set<UniqueTransaction> unknownCustomers = transactionService.getUnknownCustomerTransaction();
        BigDecimal unknownSum = unknownCustomers.stream().map(e -> e.getAmount()).reduce(BigDecimal.valueOf(0), BigDecimal::add);
        System.out.println("Balance without known user: count=" + unknownCustomers.size() + " sum=" + unknownSum + " USD");

        List<BigDecimal> known = totalKnownCustomers.stream().map(e -> e.getAmount()).sorted().collect(Collectors.toList());
        System.out.println("Balance smallest valid: " + known.get(0) + " USD");
        System.out.println("Balance largest valid: " + known.get(known.size() - 1) + " USD");
    }

}
