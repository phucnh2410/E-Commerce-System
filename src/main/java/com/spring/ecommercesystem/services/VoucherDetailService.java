package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.entities.Voucher;
import com.spring.ecommercesystem.entities.VoucherDetail;
import com.spring.ecommercesystem.entities.VoucherDetailKey;

import java.util.List;

public interface VoucherDetailService {
    public List<VoucherDetail> findAll();

    List<VoucherDetail> findVoucherDetailsByVoucherId(Long id);

    VoucherDetail findById(VoucherDetailKey id);

    public void saveAndUpdate(VoucherDetail voucherDetail);
    public void collectingVoucherForCustomer(User user, List<Voucher> vouchers);
    public Boolean collectingVoucherForCustomer(List<User> users, Voucher voucher);
    public void deleteByKey(VoucherDetailKey id);
}
