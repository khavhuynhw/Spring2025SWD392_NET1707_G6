package com.net1707.backend.service;

import com.net1707.backend.dto.CustomerDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Order;
import com.net1707.backend.model.QuizResult;
import com.net1707.backend.model.Review;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.OrderRepository;
import com.net1707.backend.service.Interface.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (customerRepository.findByPhone(customer.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Optional<Customer> getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    @Override
    public Customer updateCustomer(CustomerDTO dto) {
         Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Account does not exist"));
        if (dto.getFullName() != null && !dto.getFullName().isEmpty()) {
            customer.setFullName(dto.getFullName());
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            customer.setPhone(dto.getPhone());
        }
        if (dto.getAddress() != null && !dto.getAddress().isEmpty()) {
            customer.setAddress(dto.getAddress());
        }
        if (dto.getAgeRange() != null) {
            customer.setAgeRange(dto.getAgeRange());
        }
        if (dto.getPrimarySkinType() != null) {
            customer.setPrimarySkinType(dto.getPrimarySkinType());
        }
        if (dto.getSkinConcerns() != null && !dto.getSkinConcerns().isEmpty()) {
            customer.setSkinConcerns(dto.getSkinConcerns());
        }
        if (dto.getIsSensitive() != null) {
            customer.setIsSensitive(dto.getIsSensitive());
        }
        if (dto.getClimateZone() != null && !dto.getClimateZone().isEmpty()) {
            customer.setClimateZone(dto.getClimateZone());
        }
       return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Account does not exist"));
        customer.setVisible(false);
        customerRepository.save(customer);
    }

    @Override
    public List<Order> getCustomerOrders(Long customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getOrders)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
    }

    @Override
    public List<Review> getCustomerReviews(Long customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getReviews)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
    }

    @Override
    public List<QuizResult> getCustomerQuizResults(Long customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getQuizResults)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
    }
}
