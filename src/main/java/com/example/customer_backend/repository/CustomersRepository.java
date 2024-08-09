package com.example.customer_backend.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.customer_backend.model.Customer;

public interface CustomersRepository extends CrudRepository<Customer, Long> {
    // Custom query method to find a customer by email
    Customer findByEmail(String email);
}
