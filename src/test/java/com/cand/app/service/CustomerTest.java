package com.cand.app.service;

import com.cand.app.repository.ICustomer;
import com.cand.app.service.impl.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CustomerTest {

    private ICustomer customer;

    @BeforeEach
    public void setUp() {
        customer = Mockito.mock(ICustomer.class);
    }

    @Test
    public void shouldAnswerWithTrue() {
        Mockito.when(this.customer.findAllCustomerNames()).thenReturn(new HashSet<>(Arrays.asList("abc", "xyz")));
        ICustomerService customerService = new CustomerService(customer);
        assertArrayEquals(new String[]{"abc", "xyz"}, customerService.getAllCustomerNames().toArray(new String[0]));
    }

}
