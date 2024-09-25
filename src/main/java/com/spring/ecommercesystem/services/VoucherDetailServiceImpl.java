package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.*;
import com.spring.ecommercesystem.repositories.VoucherDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VoucherDetailServiceImpl implements VoucherDetailService {

    private final VoucherDetailRepository voucherDetailRepository;
    private final VoucherService voucherService;
    private final UserService userService;

    @Autowired
    public VoucherDetailServiceImpl(VoucherDetailRepository voucherDetailRepository, @Lazy  VoucherService voucherService, @Lazy UserService userService) {
        this.voucherDetailRepository = voucherDetailRepository;
        this.voucherService = voucherService;
        this.userService = userService;
    }

    @Override
    public VoucherDetail findById(VoucherDetailKey id) {
        return null;
    }

    @Override
    public List<VoucherDetail> findAll() {
        return this.voucherDetailRepository.findAll();
    }

    @Override
    public List<VoucherDetail> findVoucherDetailsByVoucherId(Long voucherId) {
        List<VoucherDetail> voucherDetailsFound = this.findAll().stream().filter(voucherDetail -> voucherDetail.getVoucher().getId().equals(voucherId)).collect(Collectors.toList());
        return voucherDetailsFound;
    }

    @Override
    @Transactional
    public void saveAndUpdate(VoucherDetail voucherDetail) {
        this.voucherDetailRepository.saveAndFlush(voucherDetail);
    }

    @Override
    @Transactional
    public void collectingVoucherForCustomer(User user, List<Voucher> vouchers) {
        if ( user == null ) {
            System.out.println("User or vouchers are null");
            return;
        }

        if ((vouchers == null) || (vouchers.isEmpty()) ) {
            System.out.println("List of vouchers are null");
            return;
        }

        //Get customer type from user
        User.CustomerType customerType = user.getCustomerType();

        //Get list of vouchers through customer type
        List<Voucher> customerVouchers = vouchers.stream().filter(voucher -> voucher.getCustomerType().name().equalsIgnoreCase(customerType.name())).collect(Collectors.toList());
        if (customerVouchers.isEmpty()) {
            System.out.println("No vouchers available for customer type: " + customerType);
            return;
        }


        for(Voucher voucher : customerVouchers){
            VoucherDetailKey key = new VoucherDetailKey(user.getId(), voucher.getId());
            VoucherDetail voucherDetail = new VoucherDetail();
            voucherDetail.setId(key);
            voucherDetail.setUser(user);
            voucherDetail.setVoucher(voucher);
            voucherDetail.setStatus(VoucherDetail.Status.Unused);
            this.voucherDetailRepository.saveAndFlush(voucherDetail);
        }
    }

    @Override
    @Transactional
    public Boolean collectingVoucherForCustomer(List<User> users, Voucher voucher) {
        if ( voucher == null ) {
            System.out.println("Voucher are null");
            return false;
        }

        if ((users == null) || (users.isEmpty()) ) {
            System.out.println("List of users are null");
            return false;
        }

        //Get customer type from user
        Voucher.CustomerType customerType = voucher.getCustomerType();

        //Get list of vouchers through customer type
        List<User> customerVouchers = users.stream().filter(user -> user.getCustomerType().name().equalsIgnoreCase(customerType.name())).collect(Collectors.toList());
        if (customerVouchers.isEmpty()) {
            System.out.println("No vouchers available for customer type: " + customerType);
            return false;
        }


        for(User user : customerVouchers){
            VoucherDetailKey key = new VoucherDetailKey(user.getId(), voucher.getId());
            VoucherDetail voucherDetail = new VoucherDetail();
            voucherDetail.setId(key);
            voucherDetail.setUser(user);
            voucherDetail.setVoucher(voucher);
            voucherDetail.setStatus(VoucherDetail.Status.Unused);
            this.voucherDetailRepository.saveAndFlush(voucherDetail);
        }

        return true;
    }

    @Override
    @Transactional
    public void deleteByKey(VoucherDetailKey key) {
        this.voucherDetailRepository.deleteById(key);
    }
}
