package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Voucher;

import java.util.List;
import java.util.Map;

public interface VoucherService {
    public List<Voucher> findAll();
    public Voucher findById(Long id);
    public void saveAndUpdate(Voucher voucher);

    Map<String, Object> publishingVouchers(Long voucherId);
    public void deleteById(Long id);
    public void checkAndUpdateExpiredVouchers();
}
