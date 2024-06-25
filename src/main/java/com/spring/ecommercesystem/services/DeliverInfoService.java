package com.spring.ecommercesystem.services;


import com.spring.ecommercesystem.entities.DeliveryInfo;

import java.util.List;

public interface DeliverInfoService {
    public List<DeliveryInfo> findAll();
    public DeliveryInfo findById(Long id);
    public void saveAndUpdate(DeliveryInfo deliveryInfo);
    public void deleteById(Long id);
}
