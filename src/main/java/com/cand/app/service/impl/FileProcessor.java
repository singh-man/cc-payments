/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.cand.app.service.impl;

import com.cand.app.entity.Customer;
import com.cand.app.service.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FileProcessor {

    public static final String CUSTOMER_JSON = "customer.json";

//    static {
//        Customer c1 = new Customer("Jadzia Dax", new Bank("011000015", "6622085487"));
//        Customer c2 = new Customer("James T. Kirk", new Bank("021001208", "0018423486"));
//        Customer c3 = new Customer("Jean-Luc Picard", new Bank("021001208", "1691452698"));
//        Customer c4 = new Customer("Jonathan Archer", new Bank("011000015", "3572176408"));
//        Customer c5 = new Customer("Leonard McCoy", new Bank("011000015", "8149516692"));
//        Customer c6 = new Customer("Montgomery Scott", new Bank("011000015", "7438979785"));
//        Customer c7 = new Customer("Spock", new Bank("011000015", "1690537988"),
//                new Bank("021001208", "1690537989"));
//        Customer c8 = new Customer("Wesley Crusher", new Bank("011000015", "6018423486"));
//        customers.addAll(Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8));
//    }

    @Autowired
    ITransactionService transactionService;

    @Autowired
    PopulateTransactionDB populateTransactionDB;

    @PostConstruct
    private void init() {
        Path path = Paths.get("./data/");
        List<Path> allFiles = null;
        try {
            allFiles = Files.walk(path).filter(e -> Files.isRegularFile(e)).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Failed to load files from given path \n Continuing for now: " + e.getLocalizedMessage());
        }
        List<Customer> customers = populateTransactionDB.setUpAndGetCustomers(allFiles);

        // There might be many files; better to load them in separate thread and allow the application to become responsive.
        populateTransactionDB.setAllFiles(allFiles);
        populateTransactionDB.setCustomers(customers);
        populateTransactionDB.setFileProcessor(this);

        log.info("Below must be done in a separate thread!!");
        populateTransactionDB.process(); // async operation
    }

}
