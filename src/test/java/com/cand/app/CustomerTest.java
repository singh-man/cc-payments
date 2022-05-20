package com.cand.app;

import com.cand.app.repository.ICustomer;
import com.cand.app.service.CustomerService;
import com.cand.app.service.ICustomerService;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class CustomerTest {

    @Mock
    ICustomer customer;

    @Test
    public void shouldAnswerWithTrue() {
        ICustomerService service = Mockito.mock(ICustomerService.class);
        Mockito.when(customer.findAllCustomerNames()).thenReturn(new HashSet<>(Arrays.asList("abc", "xyz")));
        CustomerService customerService = new CustomerService();
        customerService.setCustomerRep(customer);
        assertArrayEquals(new String[]{"abc", "xyz"}, customerService.getAllCustomerNames().toArray(new String[0]));

        assertTrue(true);
    }
}
