package com.cand.app.service.impl;

import com.cand.app.entity.Bank;
import com.cand.app.entity.Customer;
import com.cand.app.entity.UniqueTransaction;
import com.cand.app.exception.FileProcessFailException;
import com.cand.app.exception.Message;
import com.cand.app.json.JsonCustomer;
import com.cand.app.json.JsonTransaction;
import com.cand.app.service.ICustomerService;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Setter
@Slf4j
class PopulateTransactionDB {

    @Autowired
    ICustomerService customerService;

    private FileProcessor fileProcessor;
    private List<Customer> customers;
    private List<Path> allFiles;

//    public PopulateTransactionDB(FileProcessor fileProcessor, List<Customer> customers, List<Path> allFiles) {
//        this.fileProcessor = fileProcessor;
//        this.customers = customers;
//        this.allFiles = allFiles;
//    }

    private Predicate<Path> filterOnTransactionFiles() {
        return e -> !e.getFileName().startsWith(FileProcessor.CUSTOMER_JSON);
    }

    private Set<UniqueTransaction> prepareBankTransactionFrom(Path p, List<Customer> customers) {
        JsonTransaction trans = null;
        try {
            trans = readFileAndPrepareObject(p, JsonTransaction.class);
        } catch (IOException e) {
            log.error("Failed to load files from given path : " + e.getLocalizedMessage());
            // Do not stop processing; continue!
        }
        Set<UniqueTransaction> transacDAO = trans.transactions.stream().map(e -> {
            UniqueTransaction transactionDAO = new UniqueTransaction();
            transactionDAO.populateDAO(e);
            transactionDAO.setCustomerName(getCustomerNameForTransaction(transactionDAO, customers));
            return transactionDAO;
        }).collect(Collectors.toSet());
        return transacDAO;
    }

    private String getCustomerNameForTransaction(UniqueTransaction dao, List<Customer> customers) {
        Customer customer = customers.stream()
                .filter(e -> e.getAccounts().contains(new Bank(dao.getRoutingNumber(), dao.getAccountNumber(), e)))
                .findFirst().orElse(null);
        return customer != null ? customer.getFullName() : Customer.UNKNOWN;
    }

    private <T> T readFileAndPrepareObject(Path p, Class<T> glass) throws IOException {
        Reader reader = Files.newBufferedReader(p);
        return new ObjectMapper().readValue(reader, glass);
    }

    public List<Customer> setUpAndGetCustomers(List<Path> path) {
        Path customerPath = path.stream().filter(e -> e.getFileName().startsWith(FileProcessor.CUSTOMER_JSON))
                .findFirst().orElseThrow(() -> new FileProcessFailException(Message.CUSTOMER_FILE_NOT_FOUND));
        List<Customer> customers = null;
        try {
            customers = readFileAndPrepareObject(customerPath, JsonCustomer.class).customers;
        } catch (IOException e) {
            log.error("Failed to load customers file better to exist the processing!! : " + e.getLocalizedMessage());
            System.exit(0);
        }
        customers.forEach(c -> c.getAccounts().forEach(b -> {
            b.setCustomer(c);
            b.setBalance(BigDecimal.valueOf(0)); // init with 0 balance for each account
        }));
        customerService.saveAll(new HashSet<>(customers));
        return customers;
    }

    @Async
    public void process() {
        try {
//                Simulate reading huge number of files!!
            Thread.sleep(3000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Path> transactionFiles = allFiles.stream().filter(filterOnTransactionFiles()).collect(Collectors.toList());
        for (Path p : transactionFiles) {
            Set<UniqueTransaction> transactions = prepareBankTransactionFrom(p, customers);
            try {
                fileProcessor.transactionService.saveAll(transactions); // Inserting in bulk each file
            } catch (RuntimeException e) {
                log.error("Failed to parse and save records from file : " + p.getFileName());
                // Continue, don't stop
            }
        }
        Set<UniqueTransaction> totalKnownCustomers = new HashSet<>();
        for (Customer c : customers) {
            Set<UniqueTransaction> knownCustomerTrans = fileProcessor.transactionService.getCustomerTransaction(c.getFullName());
            totalKnownCustomers.addAll(knownCustomerTrans);
            BigDecimal sum = knownCustomerTrans.stream().map(e -> e.getAmount()).reduce(BigDecimal.valueOf(0), BigDecimal::add);
            System.out.println(String.format("Balance for %s: count=%d sum=%.2f USD", c.getFullName(),
                    knownCustomerTrans.size(), sum));
        }
        Set<UniqueTransaction> unknownCustomers = fileProcessor.transactionService.getUnknownCustomerTransaction();
        BigDecimal unknownSum = unknownCustomers.stream().map(e -> e.getAmount()).reduce(BigDecimal.valueOf(0), BigDecimal::add);
        System.out.println("Balance without known user: count=" + unknownCustomers.size() + " sum=" + unknownSum + " USD");

        List<BigDecimal> known = totalKnownCustomers.stream().map(e -> e.getAmount()).sorted().collect(Collectors.toList());
        System.out.println("Balance smallest valid: " + known.get(0) + " USD");
        System.out.println("Balance largest valid: " + known.get(known.size() - 1) + " USD");
    }

}
