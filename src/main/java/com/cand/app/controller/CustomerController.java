package com.cand.app.controller;

import com.cand.app.dto.CustomerDTO;
import com.cand.app.exception.CustomerException;
import com.cand.app.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

/**
 * Exposed REST end points
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

//    @GetMapping("pay/{user}")
//    public ResponseEntity<Customer> payCustomer(@PathVariable final String name) {
//        return new ResponseEntity<Customer>(new Customer(), HttpStatus.OK);
//    }

    @GetMapping("all")
    public ResponseEntity<Set<String>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomerNames(), HttpStatus.OK);
    }

    @GetMapping("all/{name}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable final String name) {
        return new ResponseEntity<CustomerDTO>(new CustomerDTO(customerService.getCustomerAccountDetails(name)), HttpStatus.OK);
    }

    @ExceptionHandler(value = CustomerException.class)
    public ResponseEntity customerException(CustomerException customerException) {
        return new ResponseEntity("Customer issue : " + customerException.getLocalizedMessage(), HttpStatus.CONFLICT);
    }

}
