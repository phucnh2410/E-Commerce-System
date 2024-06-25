package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Discount;
import com.spring.ecommercesystem.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public List<Discount> findAll() {
        return this.discountRepository.findAll();
    }

    @Override
    public Discount findById(Long id) {
        return this.discountRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(Discount discount) {
        this.discountRepository.saveAndFlush(discount);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.discountRepository.deleteById(id);
    }
}
