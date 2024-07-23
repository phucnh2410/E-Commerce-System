package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.OrderDetailKey;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.repositories.OrderDetailRepository;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    private final OrderService orderService;

    private final ProductService productService;

    @Autowired
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, OrderService orderService, ProductService productService) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderService = orderService;
        this.productService = productService;
    }


    @Override
    public OrderDetail addProductToOrder(Order order, Product product, int quantity) {
//        Order order = this.orderService.findById(orderId);
//        Product product = this.productService.findById(productId);

        OrderDetailKey key = new OrderDetailKey(order.getId(), product.getId());

        if (order == null && product == null){
            System.out.println("order and product is null!!!");
            return null;
        }
        OrderDetail orderDetail = new OrderDetail()
                .setId(key)
                .setOrder(order)
                .setProduct(product)
                .setProductQuantity(quantity);

        this.orderDetailRepository.saveAndFlush(orderDetail);

        return orderDetail;
    }




}
