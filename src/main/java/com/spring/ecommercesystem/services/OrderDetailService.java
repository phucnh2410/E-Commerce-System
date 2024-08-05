package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.Product;

import java.util.List;

public interface OrderDetailService {
//    public Order saveOrder(Order order);

//    public Order getOrderById(Long id);
    public List<OrderDetail> findAllItems();

    public OrderDetail addProductToOrder(Order order, Product product, int quantity);
}
