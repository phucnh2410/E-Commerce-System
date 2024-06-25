package com.spring.ecommercesystem.services;


import com.spring.ecommercesystem.entities.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {
    public List<PaymentMethod> findAll();
    public PaymentMethod findById(Long id);
    public void saveAndUpdate(PaymentMethod paymentMethod);
    public void deleteById(Long id);
}
