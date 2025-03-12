package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.CustomerDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Order;
import com.net1707.backend.model.QuizResult;
import com.net1707.backend.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Optional<Customer> getCustomerById(Long customerId);
    Page<Customer> getAllCustomers(Pageable pageable);
    Optional<Customer> getCustomerByEmail(String email);
    Optional<Customer> getCustomerByPhone(String phone);
    Customer updateCustomer(CustomerDTO dto);
    void deleteCustomer(Long customerId);
    List<Order> getCustomerOrders(Long customerId);
    List<Review> getCustomerReviews(Long customerId);
    List<QuizResult> getCustomerQuizResults(Long customerId);
}
