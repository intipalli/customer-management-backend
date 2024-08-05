package com.example.customer_backend.controller;

import com.example.customer_backend.model.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private List<Customer> customers = Arrays.asList(
        new Customer("1", "Rohan", "rohan@example.com"),
        new Customer("2", "Mitchell", "mitchell@example.com")
    );

    @GetMapping
    public String serviceHome(){
        Date d1 = new Date();
        String startTime = "Service started at: " + d1 + "\n";
        return startTime + "Customer service is up and running...";
    }
    
    @GetMapping("/customers")
    public Stream<Customer> getAll() {
        return customers.stream();
	}

    @GetMapping("/customers/{cust-id}")
    public Customer getCustomerById(@PathVariable("cust-id") String customerId) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(customerId))
                .findFirst()
                .orElse(null);
    }
}
