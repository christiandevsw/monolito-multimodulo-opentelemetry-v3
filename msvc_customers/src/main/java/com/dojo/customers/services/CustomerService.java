package com.dojo.customers.services;

import java.util.List;
import java.util.Optional;

import com.dojo.customers.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
	boolean exists(Long id);
	List<Customer> findAll();
	Page<Customer> getCustomerPage(Pageable pageable);
	List<Customer> findByLikeUsername(String username);
	Optional<Customer> findById(Long id);
	Optional<Customer> findByUsername(String username);
	Customer save(Customer customer);
}
