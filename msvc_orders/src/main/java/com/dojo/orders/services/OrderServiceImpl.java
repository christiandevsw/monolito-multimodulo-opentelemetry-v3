package com.dojo.orders.services;

import com.dojo.customers.entities.Customer;
import com.dojo.customers.services.CustomerService;
import com.dojo.orders.entities.Order;
import com.dojo.orders.entities.OrderDetail;
import com.dojo.orders.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private CustomerService customerService;
    private OrderRepository orderRepository;

    public OrderServiceImpl(CustomerService customerService, OrderRepository orderRepository) {
        this.customerService = customerService;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> listAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getPageByOrder(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Order> getOrdersByCustomer(Long id) {
        return orderRepository.findByCustomerId(id);
    }

    @Override
    public List<Order> getOrdersByUsername(String username) {
        Customer customer = customerService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado!"));
        return getOrdersByCustomer(customer.getId());
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder;
    }

    @Override
    public Order save(Long customerId, List<OrderDetail> details) {
        Customer acustomer = customerService.findById(customerId).orElse(null);
        Order order = new Order();
        order.setCustomerId(acustomer.getId());
        order.setCreateAt(LocalDate.now());
        order.setStatusOrder("Pendiente");

        details.forEach(detail -> detail.setOrder(order));
        order.setDetails(details);
        return orderRepository.save(order);
    }

    @Override
    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(String.format("Orden con id %d no existe!", orderId)));
        Optional<Customer> customer = customerService.findById(order.getCustomerId());
        if (customer.isPresent()) {
            order.setStatusOrder(status);
            return orderRepository.save(order);
        }
        throw new RuntimeException(String.format("No existe cliente con id %d", order.getCustomerId()));
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

}
