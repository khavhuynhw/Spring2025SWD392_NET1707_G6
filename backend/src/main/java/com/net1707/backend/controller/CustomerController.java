package com.net1707.backend.controller;

import com.net1707.backend.dto.CustomerDTO;
import com.net1707.backend.mapper.CustomerMapper;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Order;
import com.net1707.backend.model.QuizResult;
import com.net1707.backend.model.Review;
import com.net1707.backend.service.Interface.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    // Create a new customer
    @PostMapping("/create")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        Customer createdCustomer = customerService.createCustomer(customer);
        CustomerDTO createdCustomerDTO = customerMapper.toDto(createdCustomer);
        return new ResponseEntity<>(createdCustomerDTO, HttpStatus.CREATED);
    }

    // Get a customer by ID
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        return customer.map(c -> ResponseEntity.ok(customerMapper.toDto(c)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all customers (paginated)
    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(Pageable pageable) {
        Page<Customer> customers = customerService.getAllCustomers(pageable);
        Page<CustomerDTO> customerDTOs = customers.map(customerMapper::toDto);
        return new ResponseEntity<>(customerDTOs, HttpStatus.OK);
    }

    // Get a customer by email
    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> customer = customerService.getCustomerByEmail(email);
        return customer.map(c -> ResponseEntity.ok(customerMapper.toDto(c)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get a customer by phone
    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerDTO> getCustomerByPhone(@PathVariable String phone) {
        Optional<Customer> customer = customerService.getCustomerByPhone(phone);
        return customer.map(c -> ResponseEntity.ok(customerMapper.toDto(c)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a customer
    @PostMapping("/update")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        Customer updatedCustomer = customerService.updateCustomer(customer);
        CustomerDTO updatedCustomerDTO = customerMapper.toDto(updatedCustomer);
        return new ResponseEntity<>(updatedCustomerDTO, HttpStatus.OK);
    }

    // Soft delete a customer (set visible to false)
    @PostMapping("/delete/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all orders for a customer
    @GetMapping("/{customerId}/orders")
    public ResponseEntity<List<Order>> getCustomerOrders(@PathVariable Long customerId) {
        List<Order> orders = customerService.getCustomerOrders(customerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get all reviews for a customer
    @GetMapping("/{customerId}/reviews")
    public ResponseEntity<List<Review>> getCustomerReviews(@PathVariable Long customerId) {
        List<Review> reviews = customerService.getCustomerReviews(customerId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Get all quiz results for a customer
    @GetMapping("/{customerId}/quiz-results")
    public ResponseEntity<List<QuizResult>> getCustomerQuizResults(@PathVariable Long customerId) {
        List<QuizResult> quizResults = customerService.getCustomerQuizResults(customerId);
        return new ResponseEntity<>(quizResults, HttpStatus.OK);
    }
}
