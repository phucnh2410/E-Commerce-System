package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Voucher;
import com.spring.ecommercesystem.services.VoucherService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/voucher")
public class VoucherRestController {
    private final VoucherService voucherService;

    @Autowired
    public VoucherRestController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Voucher>> getAll(){
        List<Voucher> vouchers = this.voucherService.findAll();
        return ResponseEntity.ok().body(vouchers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getById(@PathVariable Long id){
        Voucher vouchers = this.voucherService.findById(id);
        return ResponseEntity.ok().body(vouchers);
    }





}
