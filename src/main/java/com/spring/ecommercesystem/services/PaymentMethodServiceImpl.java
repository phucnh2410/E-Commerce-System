package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.PaymentMethod;
import com.spring.ecommercesystem.repositories.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public List<PaymentMethod> findAll() {
        return this.paymentMethodRepository.findAll();
    }

    @Override
    public PaymentMethod findById(Long id) {
        return this.paymentMethodRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(PaymentMethod paymentMethod) {
        this.paymentMethodRepository.saveAndFlush(paymentMethod);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.paymentMethodRepository.deleteById(id);
    }
}
