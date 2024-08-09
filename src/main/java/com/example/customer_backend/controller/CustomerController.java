package com.example.customer_backend.controller;

import com.example.customer_backend.model.Customer;
import com.example.customer_backend.repository.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.Iterator;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    CustomersRepository customersRepository;

    @GetMapping
    public String serviceHome() {
        Date d1 = new Date();
        String startTime = "Service started at: " + d1 + "\n";
        return startTime + "Customer service is up and running...";
    }

    @GetMapping("/customers")
    public ResponseEntity<Iterable<Customer>> getAll() {
        Iterable<Customer> customers = customersRepository.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable("customerId") long id) {
        java.util.Optional<Customer> customerOptional = customersRepository.findById(id);

        if (customerOptional.isPresent()) {
            return ResponseEntity.ok(customerOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + id);
        }
    }

    @GetMapping("/customers/byname/{username}")
    public ResponseEntity<?> getCustomerByName(@PathVariable("username") String name, UriComponentsBuilder uri) {
        Iterator<Customer> customers = customersRepository.findAll().iterator();
        while (customers.hasNext()) {
            Customer cust = customers.next();
            if (cust.getEmail().equalsIgnoreCase(name)) {
                return ResponseEntity.ok(cust);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/customers")
    public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer, UriComponentsBuilder uri) {
        if (newCustomer.getId() != 0) {
            return ResponseEntity.badRequest().body("Customer ID must not be provided, it will be generated automatically.");
        }
        if (newCustomer.getName() == null || newCustomer.getEmail() == null) {
            return ResponseEntity.badRequest().body("Name and Email must not be null.");
        }

        Customer existingCustomer = customersRepository.findByEmail(newCustomer.getEmail());
        if (existingCustomer != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A customer with this email already exists.");
        }

        newCustomer = customersRepository.save(newCustomer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newCustomer.getId()).toUri();

        return ResponseEntity.created(location).body("Customer created successfully.");
    }

    @PutMapping("/customers/{customerId}")
    public ResponseEntity<?> putCustomer(
            @RequestBody Customer updatedCustomer,
            @PathVariable("customerId") long customerId) {

        if (updatedCustomer.getName() == null || updatedCustomer.getEmail() == null) {
            return ResponseEntity.badRequest().body("Name and Email must not be null.");
        }

        java.util.Optional<Customer> existingCustomerOptional = customersRepository.findById(customerId);

        if (existingCustomerOptional.isPresent()) {
            Customer existingCustomer = existingCustomerOptional.get();

            Customer customerWithEmail = customersRepository.findByEmail(updatedCustomer.getEmail());
            if (customerWithEmail != null && customerWithEmail.getId() != customerId) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use by another customer.");
            }

            existingCustomer.setName(updatedCustomer.getName());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            existingCustomer.setPassword(updatedCustomer.getPassword());

            customersRepository.save(existingCustomer);

            return ResponseEntity.ok().body("Customer updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable("customerId") long customerId) {
        java.util.Optional<Customer> customerOptional = customersRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            customersRepository.deleteById(customerId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Customer deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with ID: " + customerId);
        }
    }

}
