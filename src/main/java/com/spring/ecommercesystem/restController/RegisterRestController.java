package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Category;
import com.spring.ecommercesystem.entities.Role;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.entities.Voucher;
import com.spring.ecommercesystem.services.*;
import jakarta.servlet.http.HttpSession;
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

    private final MailService mailService;
    private final VoucherService voucherService;
    private final VoucherDetailService voucherDetailService;

    private final OtpStorageService otpStorageService;

    @Autowired
    public RegisterRestController(UserService userService, RoleService roleService, MailService mailService, VoucherService voucherService, VoucherDetailService voucherDetailService, OtpStorageService otpStorageService) {
        this.userService = userService;
        this.roleService = roleService;
        this.mailService = mailService;
        this.voucherService = voucherService;
        this.voucherDetailService = voucherDetailService;
        this.otpStorageService = otpStorageService;
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


    @PostMapping("/send-OTP-by-email/{email}")
    public ResponseEntity<Map<String, Object>> forgotPassword(@PathVariable String email){
        Map<String, Object> response = new HashMap<>();

        System.out.println("Email: "+email);

        User user = this.userService.findByEmail(email);
        if (user == null){
            response.put("message", "This email does not exist, please enter a valid email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        try{
            String otpCode = this.otpStorageService.generateOtpCode();

            this.mailService.sendOtpByMail(user.getEmail(), otpCode);
            this.otpStorageService.storeOtp(user.getEmail(), otpCode);

            response.put("message", "The OTP was sent to your email, please enter a OTP to verify");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            System.err.println("Error while creating category: " + e.getMessage());
            response.put("message", "Sorry, something went wrong. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/verify-OTP/{otp}/{email}")
    public ResponseEntity<Map<String, Object>> verifyOTP(@PathVariable String otp, @PathVariable String email, HttpSession session){
        Map<String, Object> response = new HashMap<>();

        System.out.println("OTP: "+otp);
        System.out.println("Email: "+email);

        boolean isCorrect = this.otpStorageService.verifyOtp(email, otp);

        if (!isCorrect){
            response.put("message", "This OTP incorrect, please re-check your message in email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        session.setAttribute("emailUserChangePassword", email);
        response.put("message", "success");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/save-change-password/{password}")
    public ResponseEntity<Map<String, Object>> saveChangePassword(@PathVariable String password, HttpSession session){
        String email = (String) session.getAttribute("emailUserChangePassword");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Map<String, Object> response = new HashMap<>();

        System.out.println("Password: "+password);
        System.out.println("Email: "+email);

//        boolean isCorrect = this.otpStorageService.verifyOtp(email, otp);

        if (password.isEmpty() || password.equals(null)){
            response.put("message", "There was an error during the change, please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        User user = this.userService.findByEmail(email);

        user.setPassword(encoder.encode(password));

        this.userService.saveAndUpdate(user);

        session.removeAttribute("emailUserChangePassword");

        response.put("user", user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

































}
