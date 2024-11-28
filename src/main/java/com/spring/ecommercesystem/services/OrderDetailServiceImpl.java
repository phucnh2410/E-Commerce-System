package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.OrderDetailKey;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.repositories.OrderDetailRepository;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<OrderDetail> findAllItems() {
        return this.orderDetailRepository.findAll();
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
                .setProductQuantity(quantity)
                .setProductStatus(OrderDetail.ProductStatus.Pending);

        this.orderDetailRepository.saveAndFlush(orderDetail);

        return orderDetail;
    }

    @Override
    public OrderDetail findByOrderIdAndProductId(Long orderId, Long productId) {
        return null;
    }

    @Override
    public OrderDetail updateStatusForEachProduct(Long orderId, Long productId) {
        // Tìm OrderDetail theo OrderId và ProductId
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailByOrderIdAndProductId(orderId, productId);

        if (orderDetail == null) {
            // Nếu không tìm thấy, có thể ném Exception hoặc trả về null tùy theo yêu cầu
            System.out.println("OrderDetail not found for orderId " + orderId + " and productId " + productId);
            return null;
        }

        // Cập nhật thông tin OrderDetail
        orderDetail.setProductStatus(OrderDetail.ProductStatus.Confirmed);

        // Lưu lại OrderDetail đã được cập nhật
        this.orderDetailRepository.saveAndFlush(orderDetail);

        return orderDetail;
    }
}
