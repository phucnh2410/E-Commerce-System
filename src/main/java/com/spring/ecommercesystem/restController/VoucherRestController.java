package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Voucher;
import com.spring.ecommercesystem.services.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Voucher voucher = this.voucherService.findById(id);
        return ResponseEntity.ok().body(voucher);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveVoucher(@RequestBody Voucher voucher){
        Map<String, Object> response = new HashMap<>();
        if (voucher == null){
            response.put("message", "Please fill full to add new voucher!!!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (voucher.getId() != null){
            response.put("message", "This voucher was updated successfully!!!");
            voucher.setStatus(Voucher.Status.New);
            this.voucherService.saveAndUpdate(voucher);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        response.put("message", "Voucher was added successfully!!!");
        voucher.setStatus(Voucher.Status.New);
        this.voucherService.saveAndUpdate(voucher);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/publish_all")
    public ResponseEntity<Map<String, Object>> publishAllVoucher(){
        Map<String, Object> response;

        response = this.voucherService.publishingVouchers(null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/publish/{id}")
    public ResponseEntity<Map<String, Object>> publishVoucherById(@PathVariable Long id){
        Map<String, Object> response;

        response = this.voucherService.publishingVouchers(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable Long id){
        try{
            this.voucherService.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



}
