package com.cand.app.service;

import com.cand.app.entity.Customer;
import com.cand.app.entity.UniqueTransaction;

import java.util.List;
import java.util.Set;

public interface ICustomerService extends IService<Customer> {

    Set<String> getAllCustomerNames();

    Customer getCustomerAccountDetails(String name);
}

