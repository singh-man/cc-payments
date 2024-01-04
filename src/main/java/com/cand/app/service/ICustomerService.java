package com.cand.app.service;

import com.cand.app.entity.Customer;
import com.cand.app.entity.UniqueTransaction;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface ICustomerService extends IService<Customer> {

    Set<String> getAllCustomerNames();

    Customer getCustomerAccountDetails(String name);

    Boolean addXAmountToCustomerAfterYTime(String name, float amt, int time);

    List<Customer> saveAllFrom(List<Path> path);

}

