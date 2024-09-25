package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return this.orderRepository.findById(id).get();
    }

    @Override
    public List<Order> findOrdersById(Long id) {
        return this.orderRepository.findOrdersById(id);
    }

    @Override
    @Transactional
    public void saveAndUpdate(Order order) {
        this.orderRepository.saveAndFlush(order);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.orderRepository.deleteById(id);
    }

    @Override
    public Double autoCalculatingExpenditure(User user) {
        List<Order> orders = user.getOrders();

        Double expenditure = orders.stream().filter(order -> order.getStatus().equals(Order.Status.Received)).mapToDouble(Order::getTotalAmount).sum();

        return expenditure;
    }


}
