package com.dojo.customers.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dojo.customers.entities.Customer;
import com.dojo.customers.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{
	private CustomerRepository repository;
	public CustomerServiceImpl(CustomerRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean exists(Long id) {
		return repository.existsById(id);
	}

	@Override
	public List<Customer> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<Customer> getCustomerPage(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public List<Customer> findByLikeUsername(String username) {
		return repository.findByLikeUsername(username);
	}

	@Override
	public Optional<Customer> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Optional<Customer> findByUsername(String username) {
		return repository.findByUsername(username);
	}

	@Override
	public Customer save(Customer customer) {
		return repository.save(customer);
	}

}
