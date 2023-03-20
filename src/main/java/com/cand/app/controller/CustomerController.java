package com.cand.app.controller;

import com.cand.app.dto.CustomerDTO;
import com.cand.app.exception.CustomerException;
import com.cand.app.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Exposed REST end points
 */
@RestController //combination of @Component and @ResponseBody or if using @Controller use @RespoB... at method level.
@RequestMapping("/customer")
@SecurityRequirement(name = "javainuseapi") // adds a lock sign and if on method lock sign only on that method
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

    @GetMapping("name/{name}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable final String name) {
        return ResponseEntity.ok(new CustomerDTO(customerService.getCustomerAccountDetails(name)));
    }

    @GetMapping(value = "/{name}")
    @Operation(summary = "I will get the ID of the customer")
    public long getCustomerId(@PathVariable final String name) {
        return customerService.getCustomerAccountDetails(name).getId();
    }

    @ExceptionHandler(value = CustomerException.class)
    public ResponseEntity customerException(CustomerException customerException) {
        return new ResponseEntity("Customer issue : " + customerException.getLocalizedMessage(), HttpStatus.CONFLICT);
    }

}
