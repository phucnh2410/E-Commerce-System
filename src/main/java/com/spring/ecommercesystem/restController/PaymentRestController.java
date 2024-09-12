package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.PaymentMethod;
import com.spring.ecommercesystem.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentRestController {
    private final PaymentMethodService paymentService;

    @Autowired
    public PaymentRestController(PaymentMethodService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentMethod>>getAllPaymentMethod(){
        List<PaymentMethod> paymentMethods = this.paymentService.findAll();
        return ResponseEntity.ok().body(paymentMethods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod>getById(@PathVariable Long id){
        PaymentMethod paymentMethods = this.paymentService.findById(id);
        return ResponseEntity.ok().body(paymentMethods);
    }
}
