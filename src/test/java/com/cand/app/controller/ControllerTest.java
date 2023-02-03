package com.cand.app.controller;

import com.cand.app.entity.Customer;
import com.cand.app.service.ICustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControllerTest.class)
@ComponentScan(basePackages = {"com.cand.app.controller"})
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ICustomerService customer;

    @Test
    public void checkSpockDoesNotExistInDB() throws Exception {
        Customer murphy = new Customer("Murphy");
        murphy.setId(10);
//        BDDMockito.given(customer.getAllCustomerNames()).
//                willReturn(new HashSet<>(Arrays.asList(murphy.getFullName())));
        // BDDMockito above can also be used and is also a newer way of testing
        Mockito.when(customer.getAllCustomerNames()).thenReturn(new HashSet<>(Arrays.asList("Murphy")));
        String contentAsString = mvc.perform(get("/customer/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals("[\"Murphy\"]", contentAsString);
        Assertions.assertNotEquals("[\"Spock\"]", contentAsString);
    }

    @Test
    public void checkSpockExistInDB() throws Exception {
        Customer spock = new Customer("Spock");
        spock.setId(11);
        Customer murphy = new Customer("Murphy");
        spock.setId(10);
        BDDMockito.given(customer.getAllCustomerNames()).willReturn(new HashSet<>(Arrays.asList(spock.getFullName(), murphy.getFullName())));
        mvc.perform(get("/customer/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(spock.getFullName())));

    }

    @Test
    public void checkSpockID() throws Exception {
        Customer spock = new Customer("Spock");
        spock.setId(11);
        Mockito.when(customer.getCustomerAccountDetails("Spock")).thenReturn(spock);
        mvc.perform(get("/customer/Spock").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(spock.getId() + ""));
    }
}
