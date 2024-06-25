package com.spring.ecommercesystem.services;



import com.spring.ecommercesystem.entities.Discount;

import java.util.List;

public interface DiscountService {
    public List<Discount> findAll();
    public Discount findById(Long id);
    public void saveAndUpdate(Discount discount);
    public void deleteById(Long id);
}
