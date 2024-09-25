package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Role;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.entities.Voucher;
import com.spring.ecommercesystem.services.RoleService;
import com.spring.ecommercesystem.services.UserService;
import com.spring.ecommercesystem.services.VoucherDetailService;
import com.spring.ecommercesystem.services.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/register")
public class RegisterRestController {
    private final UserService userService;
    private final RoleService roleService;
    private final VoucherService voucherService;
    private final VoucherDetailService voucherDetailService;

    @Autowired
    public RegisterRestController(UserService userService, RoleService roleService, VoucherService voucherService, VoucherDetailService voucherDetailService) {
        this.userService = userService;
        this.roleService = roleService;
        this.voucherService = voucherService;
        this.voucherDetailService = voucherDetailService;
    }

    @GetMapping()
    public ResponseEntity<List<Role>> getUserAuthentication(){
        List<Role> roles = this.roleService.findAll();
        return ResponseEntity.ok(roles);
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user){
        Map<String, Object> response = new HashMap<>();

        if (user == null){
            response.put("message", "User object is null!!!");
            return ResponseEntity.badRequest().body(response);
        }

        String email = user.getEmail();
        User userExisted = this.userService.findByEmail(email);
        if (userExisted != null){
            response.put("message", "Email already exists!, Please enter another email");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedDate(new Date(System.currentTimeMillis()));
        user.setCustomerType(User.CustomerType.NEW);
        user.setExpenditure(0.0);

        this.userService.saveAndUpdate(user);
        List<Voucher> vouchers = this.voucherService.findAll().stream().filter(voucher -> voucher.getStatus() == Voucher.Status.Published).collect(Collectors.toList());
        this.voucherDetailService.collectingVoucherForCustomer(user, vouchers);

        response.put("message", "The account was registered successfully, Please sign in with this account");
        response.put("user", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
