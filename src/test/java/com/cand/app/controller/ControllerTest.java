package com.cand.app.controller;

import com.cand.app.entity.Customer;
import com.cand.app.service.ICustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControllerTest.class)
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ICustomerService customer;

    @Test
    public void checkSpockExistInDB() throws Exception {
        Customer spock = new Customer("Spock"); spock.setId(11);
        Customer murphy = new Customer("Murphy"); spock.setId(10);
        BDDMockito.given(customer.getAllCustomerNames()).willReturn(new HashSet<>(Arrays.asList(spock.getFullName(), murphy.getFullName())));
        mvc.perform(get("/customer/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(spock.getFullName())));

        Mockito.when(customer.getAllCustomerNames()).thenReturn(new HashSet<>(Arrays.asList("Jack-1")));
        mvc.perform(get("/customer/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Jack-1")));
    }

    @Test
    public void checkSpockID() throws Exception {
        Customer spock = new Customer("Spock"); spock.setId(11);
        Mockito.when(customer.getCustomerAccountDetails("Spock")).thenReturn(spock);
        mvc.perform(get("/customer/Spock").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(spock.getId() + ""));
    }
}
