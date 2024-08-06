package com.example.customer_backend.controller;

import com.example.customer_backend.model.Customer;
import com.example.customer_backend.repository.CustomersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Iterable<Customer> getAll() {
        return customersRepository.findAll();
    }

    @GetMapping("/customers/{customerId}")
    public java.util.Optional<Customer> getCustomerById(@PathVariable("customerId") long id) {
        return customersRepository.findById(id);
    }

    @GetMapping("/customers/byname/{username}")
    public ResponseEntity<?> getCustomerByName(@PathVariable("username") String name, UriComponentsBuilder uri) {
        
        Iterator<Customer> customers = customersRepository.findAll().iterator();
		while(customers.hasNext()) {
			Customer cust = customers.next();
			if(cust.getName().equalsIgnoreCase(name)) {
				ResponseEntity<?> response = ResponseEntity.ok(cust);
				return response;				
			}	
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        
    }

    @PostMapping("/customers")
    public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer, UriComponentsBuilder uri) {
        if (newCustomer.getId() != 0 || newCustomer.getName() == null || newCustomer.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        newCustomer = customersRepository.save(newCustomer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newCustomer.getId()).toUri();
        ResponseEntity<?> response = ResponseEntity.created(location).build();
        return response;
    }

    @PutMapping("/customers/{customerId}")
    public ResponseEntity<?> putCustomer(
            @RequestBody Customer newCustomer,
            @PathVariable("customerId") long customerId) {
        if (newCustomer.getId() != customerId || newCustomer.getName() == null || newCustomer.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        newCustomer = customersRepository.save(newCustomer);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable("customerId") long id) {
        customersRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
