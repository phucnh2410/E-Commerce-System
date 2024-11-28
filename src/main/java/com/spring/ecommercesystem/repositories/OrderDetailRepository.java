package com.spring.ecommercesystem.repositories;

import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.OrderDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey> {
    OrderDetail findOrderDetailByOrderIdAndProductId (Long orderId, Long productId);
}
