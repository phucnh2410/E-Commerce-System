package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.services.OrderDetailService;
import com.spring.ecommercesystem.services.OrderService;
import com.spring.ecommercesystem.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class ConfirmOrderController {
    private final OrderService orderService;

    private final ProductService productService;

    private final OrderDetailService orderDetailService;

    @Autowired
    public ConfirmOrderController(OrderService orderService, ProductService productService, OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderDetailService = orderDetailService;
    }


}
