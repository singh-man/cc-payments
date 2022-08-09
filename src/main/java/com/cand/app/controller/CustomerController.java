package com.cand.app.controller;

import com.cand.app.dto.CustomerDTO;
import com.cand.app.exception.CustomerException;
import com.cand.app.service.ICustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/balance")
//    @RequestMapping(method = RequestMethod.POST)
//    public ResponseEntity<CustomerDTO> withdrawAmount(@RequestBody final CustomerBalanceRequest balanceRequest) {
//        return new ResponseEntity<CustomerDTO>(customerService.balance(balanceRequest), HttpStatus.OK);
//    }

    @GetMapping("all")
    public ResponseEntity<Set<String>> getAllCustomers() {
        // ResponseEntity holds the http status and headers otherwise send the obj only
        return new ResponseEntity<>(customerService.getAllCustomerNames(), HttpStatus.OK);
    }

    @GetMapping("all/{name}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable final String name) {
        return ResponseEntity.ok(new CustomerDTO(customerService.getCustomerAccountDetails(name)));
    }

    @GetMapping(value = "/{name}")
    @ApiOperation("I will get the ID of the customer")
    @ResponseBody
    public long getCustomerId(@PathVariable final String name) {
        return customerService.getCustomerAccountDetails(name).getId();
    }

    @ExceptionHandler(value = CustomerException.class)
    public ResponseEntity customerException(CustomerException customerException) {
        return new ResponseEntity("Customer issue : " + customerException.getLocalizedMessage(), HttpStatus.CONFLICT);
    }

}
