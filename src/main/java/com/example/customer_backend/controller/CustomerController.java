package com.example.customer_backend.controller;

import com.example.customer_backend.model.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private List<Customer> customers = Arrays.asList(
        new Customer("1", "Rohan", "rohan@example.com"),
        new Customer("2", "Mitchell", "mitchell@example.com")
    );

    @GetMapping
    public String home() {
        return "Customer API is up and running.";
    }

    @GetMapping("/customers/{cust-id}")
    public Customer getCustomerById(@PathVariable("cust-id") String customerId) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(customerId))
                .findFirst()
                .orElse(null);
    }
}
