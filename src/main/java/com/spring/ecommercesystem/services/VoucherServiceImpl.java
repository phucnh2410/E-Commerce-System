package com.spring.ecommercesystem.services;


import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.entities.Voucher;
import com.spring.ecommercesystem.entities.VoucherDetail;
import com.spring.ecommercesystem.entities.VoucherDetailKey;
import com.spring.ecommercesystem.repositories.VoucherDetailRepository;
import com.spring.ecommercesystem.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherDetailRepository voucherDetailRepository;

    private final VoucherDetailService voucherDetailService;
    private final UserService userService;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository, VoucherDetailRepository voucherDetailRepository, VoucherDetailService voucherDetailService, UserService userService) {
        this.voucherRepository = voucherRepository;
        this.voucherDetailRepository = voucherDetailRepository;
        this.voucherDetailService = voucherDetailService;
        this.userService = userService;
    }

    @Override
    public List<Voucher> findAll() {
        return this.voucherRepository.findAll();
    }

    @Override
    public Voucher findById(Long id) {
        return this.voucherRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(Voucher voucherRequested) {
        this.voucherRepository.saveAndFlush(voucherRequested);
    }

    @Override
    public Map<String, Object> publishingVouchers(Long voucherId) {
        Map<String, Object> response = new HashMap<>();
        List<User> customers = this.userService.findAll().stream().filter(user -> user.getRole().getName().equals("ROLE_CUSTOMER")).collect(Collectors.toList());

        //Publish voucher by ID
        if (voucherId != null){
            Voucher newVoucher = this.findById(voucherId);

            String cusType = newVoucher.getCustomerType().name();
            List<User> usersMatchType = this.filterCustomerByCustomerType(customers, cusType);
            Boolean isSuccess = this.voucherDetailService.collectingVoucherForCustomer(usersMatchType, newVoucher);
            newVoucher.setStatus(Voucher.Status.Published);

            this.saveAndUpdate(newVoucher);
            response.put("message", "This voucher was published successful!!!");

        } else {//Publish all voucher
            List<Voucher> newVouchers = this.findAll().stream().filter(voucher -> voucher.getStatus() == Voucher.Status.New).collect(Collectors.toList());

            newVouchers.forEach(voucher -> {
                String cusType = voucher.getCustomerType().name();
                List<User> usersMatchType = this.filterCustomerByCustomerType(customers, cusType);
                this.voucherDetailService.collectingVoucherForCustomer(usersMatchType, voucher);
                voucher.setStatus(Voucher.Status.Published);
                this.saveAndUpdate(voucher);
            });

            response.put("message", "All vouchers were published successful!!!");
        }
        return response;
    }

    public List<User> filterCustomerByCustomerType(List<User> customers, String customerType){
        List<User> customerMatchType = customers.stream().filter(user -> user.getCustomerType().name().equalsIgnoreCase(customerType)).collect(Collectors.toList());
        return customerMatchType;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        List<VoucherDetail> voucherDetails = this.voucherDetailService.findVoucherDetailsByVoucherId(id);
        if (voucherDetails.isEmpty()){
            this.voucherRepository.deleteById(id);
            return;
        }

        voucherDetails.forEach(voucherDetail -> {
            VoucherDetailKey key = new VoucherDetailKey(voucherDetail.getVoucher().getId(), voucherDetail.getUser().getId());
            this.voucherDetailService.deleteByKey(key);
        });
        this.voucherRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void checkAndUpdateExpiredVouchers() {
        List<Voucher> vouchers = this.findAll();
        Date currentDay = new Date(System.currentTimeMillis());

        vouchers.forEach(voucher -> {
            if (voucher.getExpiryDate().before(currentDay)) {
                voucher.setStatus(Voucher.Status.Expired);
                this.saveAndUpdate(voucher);
            }
        });

    }
}
