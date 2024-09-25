package com.spring.ecommercesystem.services;


import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.User;

import java.util.List;

public interface OrderService {
    public List<Order> findAll();
    public Order findById(Long id);
    public List<Order> findOrdersById(Long id);
    public void saveAndUpdate(Order order);
    public void deleteById(Long id);
    public Double autoCalculatingExpenditure(User user);
}
