//package com.spring.ecommercesystem;
//
//import com.spring.ecommercesystem.entities.User;
//import com.spring.ecommercesystem.entities.Voucher;
//import com.spring.ecommercesystem.services.ProductService;
//import com.spring.ecommercesystem.services.UserService;
//import com.spring.ecommercesystem.services.VoucherDetailService;
//import com.spring.ecommercesystem.services.VoucherService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.sql.Date;
//import java.time.LocalDate;
//import java.time.Period;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//public class ScheduledTasks {
//
//    private final UserService userService;
//    private final VoucherService voucherService;
//    private final VoucherDetailService voucherDetailService;
//
//    @Autowired
//    public ScheduledTasks(UserService userService, VoucherService voucherService, VoucherDetailService voucherDetailService) {
//        this.userService = userService;
//        this.voucherService = voucherService;
//        this.voucherDetailService = voucherDetailService;
//    }
//
////    @Scheduled(cron = "0 0 21 * * ?")// It runs every day at midnight (0h)
////    public void updateCustomerType() {
////        List<User> users = userService.findAll().stream().filter(user -> user.getCustomerType().equals(User.CustomerType.NEW)).collect(Collectors.toList());
////
////        for (User user : users) {
////            User.CustomerType customerType = determineCustomerType(user.getCreatedDate());
////            user.setCustomerType(customerType);
////            this.userService.saveAndUpdate(user);
////
////            //Re-collect voucher for Loyal customer
////            List<Voucher> vouchers = this.voucherService.findAll();
////            this.voucherDetailService.collectingVoucherForCustomer(user, vouchers);
////        }
////    }
////
////    private User.CustomerType determineCustomerType(Date registrationDate) {
////        LocalDate regDate = registrationDate.toLocalDate();
////        LocalDate now = LocalDate.now();
////        Period period = Period.between(regDate, now);
////
////        if (period.getDays() <= 30) {
////            log.info("New customers were set successful!!!");
////        }
////
////        log.info("Loyal customers were set successful!!!");
////        return User.CustomerType.LOYAL;
////    }
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void checkExpiredVouchers(){
//        this.voucherService.checkAndUpdateExpiredVouchers();
//    }
//
//
//
//}
