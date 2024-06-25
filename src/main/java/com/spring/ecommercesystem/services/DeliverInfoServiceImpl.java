package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.DeliveryInfo;
import com.spring.ecommercesystem.repositories.DeliveryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeliverInfoServiceImpl implements DeliverInfoService {
    private final DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    public DeliverInfoServiceImpl(DeliveryInfoRepository deliveryInfoRepository) {
        this.deliveryInfoRepository = deliveryInfoRepository;
    }


    @Override
    public List<DeliveryInfo> findAll() {
        return this.deliveryInfoRepository.findAll();
    }

    @Override
    public DeliveryInfo findById(Long id) {
        return this.deliveryInfoRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(DeliveryInfo deliveryInfo) {
        this.deliveryInfoRepository.saveAndFlush(deliveryInfo);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.deliveryInfoRepository.deleteById(id);
    }
}
