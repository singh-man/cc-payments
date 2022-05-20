package com.cand.app.repository;

import com.cand.app.entity.UniqueTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface IBankTransaction extends CrudRepository<UniqueTransaction, String> {

    List<UniqueTransaction> findByCustomerName(String customerName);

    @Query("select u from UniqueTransaction u where u.customerName != :name")
    Set<UniqueTransaction> findAllKnownTransactionsFor(String name);

}
