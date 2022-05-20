package com.cand.app.repository;

import com.cand.app.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ICustomer extends CrudRepository<Customer, String> {

    Customer findByFullName(String customerName);

    @Query("select distinct fullName from Customer")
    Set<String> findAllCustomerNames();
    //    List<String> findAllDistinctByCustomerName();

}
