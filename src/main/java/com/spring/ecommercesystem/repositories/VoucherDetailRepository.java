package com.spring.ecommercesystem.repositories;

import com.spring.ecommercesystem.entities.VoucherDetail;
import com.spring.ecommercesystem.entities.VoucherDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherDetailRepository extends JpaRepository<VoucherDetail, VoucherDetailKey> {
}
