package com.cand.app.service;

import com.cand.app.entity.Customer;
import com.cand.app.entity.UniqueTransaction;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface ITransactionService extends IService<UniqueTransaction> {

    Set<UniqueTransaction> getCustomerTransaction(String customer);

    Set<UniqueTransaction> getUnknownCustomerTransaction();

    Set<UniqueTransaction> getAllKnownCustomerTransaction();

    Set<UniqueTransaction> getAllCustomersTransaction(List<Customer> customer);

}

