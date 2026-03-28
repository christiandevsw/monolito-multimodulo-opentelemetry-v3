package com.dojo.customers.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.azure.storage.blob.BlobClient;
import com.dojo.customers.services.CustomerBlobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dojo.customers.entities.Customer;
import com.dojo.customers.services.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	private Logger logger = LoggerFactory.getLogger(CustomerController.class);
	private CustomerService service;
	private CustomerBlobService storageService;

	public CustomerController(CustomerService service,CustomerBlobService storageService) {
		this.service = service;
		this.storageService = storageService;
	}

	@RequestMapping(method =  RequestMethod.HEAD,path = "/{id}")
	public ResponseEntity<Void> existsById(@PathVariable("id") Long id) {
		if(service.exists(id)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/all")
	public ResponseEntity<List<Customer>> findAll() {
		logger.info("Clientes consultados: "+ Map.of("total", service.findAll().size()));
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping
	public ResponseEntity<List<Customer>> getCustomerByPage(Pageable pageable) {
		Page<Customer> customerPage = service.getCustomerPage(pageable);
		logger.info("Clientes consultados: "+ Map.of("total", customerPage.getNumberOfElements()));
		return ResponseEntity.ok(customerPage.getContent());
	}


	@GetMapping("search")
	public ResponseEntity<List<Customer>> findByLikeUsername(@RequestParam String username) {
		logger.info("Clientes consultados: "+ Map.of("total", service.findAll().size()));
		return ResponseEntity.ok(service.findByLikeUsername(username));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		Optional<Customer> optionalCustomer =service.findById(id);
		if(optionalCustomer.isPresent()) {
			logger.info("Datos de cliente: "+optionalCustomer.get());
			return ResponseEntity.ok(optionalCustomer.get());
		}
		logger.warn("Cliente con id: "+id+" no encontrado!");
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Collections.singletonMap("Cliente","No encontrado"));
	}

	@GetMapping("/by-user/{username}")
	public ResponseEntity<?> findByUsername(@PathVariable String username) {
		Optional<Customer> optional=service.findByUsername(username);
		if(optional.isEmpty()) {
			logger.warn("Cliente con username: "+username+" no encontrado!");
			return new ResponseEntity<>(Collections.singletonMap("Cliente","No encontrado"), HttpStatus.NOT_FOUND);
		}
		logger.info("Datos de cliente: " +username);
		return ResponseEntity.ok(optional.get());
	}

	//v1
	@PostMapping
	public ResponseEntity<Customer> save(@RequestBody Customer customer) {
		logger.info("Nuevo cliente: "+ customer.toString());
		return ResponseEntity.ok(service.save(customer));
	}

	@GetMapping("/blob/names")
	public ResponseEntity<List<String>> listBlobNames() {
		logger.info("Total de elementos del blobStorage: "+ storageService.listBlobNames().size());
		return ResponseEntity.ok(storageService.listBlobNames());
	}

	@GetMapping("/blob/{name}")
	public ResponseEntity<?> getBlobUrl(@PathVariable String name) {
		Optional<BlobClient> optionalBlobClient = storageService.findFileBlob(name);
		if (optionalBlobClient.isPresent()){
			String urlBlob=optionalBlobClient.get().getBlobUrl();
			logger.info("Url del blob encontrado: "+urlBlob);
			return ResponseEntity.ok(urlBlob);
		}
		logger.info("No se encontró archivo Blob!");
		return ResponseEntity.notFound().build();
	}


}
