package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void saveAndUpdate(Order order) {
        this.orderRepository.saveAndFlush(order);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.orderRepository.deleteById(id);
    }
}
