package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Voucher;

import java.util.List;

public interface VoucherService {
    public List<Voucher> findAll();
    public Voucher findById(Long id);
    public void saveAndUpdate(Voucher voucher);
    public void deleteById(Long id);
}
