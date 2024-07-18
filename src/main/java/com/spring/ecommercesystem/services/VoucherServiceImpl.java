package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Voucher;
import com.spring.ecommercesystem.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
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
    public void saveAndUpdate(Voucher voucher) {
        this.voucherRepository.saveAndFlush(voucher);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.voucherRepository.deleteById(id);
    }
}
