package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.*;
import com.spring.ecommercesystem.services.*;
import com.spring.ecommercesystem.temp.CartTemp;
import com.spring.ecommercesystem.temp.OrderTemp;
import com.spring.ecommercesystem.temp.UserCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final OrderService orderService;

    private final UserService userService;

    private final CategoryService categoryService;

    private final ProductService productService;

    private final VoucherService voucherService;

    @Autowired
    public AdminController(OrderService orderService, UserService userService, CategoryService categoryService, ProductService productService, VoucherService voucherService) {
        this.orderService = orderService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.voucherService = voucherService;
    }


    @GetMapping
    public String showAdminPage(){
        return "Admin/adminDashboard";
    }


    @GetMapping("/orderFragment")
    public String orderFrag (Model model){
        List<Order> orders = this.orderService.findAll();
        List<Order> ordersSorted = orders.stream().sorted(Comparator.comparing(Order::getId).reversed()).collect(Collectors.toList());//descending order

        model.addAttribute("orders", ordersSorted);
        model.addAttribute("totalOrder", orders.size());
        return "Admin/adminDashboard :: orderManagementFrag";
    }

    @GetMapping("/orderFragment/{id}")
    public String searchOrderById (@PathVariable Long id, Model model){
        List<Order> orders = this.orderService.findOrdersById(id);
        if (orders.isEmpty()) {
            model.addAttribute("message", "No orders found.");
//            return "Admin/adminDashboard :: orderMessageFrag";
        }else {
//        List<Order> ordersSorted = orders.stream().sorted(Comparator.comparing(Order::getId).reversed()).collect(Collectors.toList());//descending order
            model.addAttribute("orders", orders);
        }

        return "Admin/adminDashboard :: orderManagementFrag";
    }

    @GetMapping("/voucherFragment/{status}")
    public String voucherFrag(@PathVariable String status, Model model){
        List<Voucher> vouchers = this.voucherService.findAll();
        List<Voucher> vouchersFiltered = vouchers.stream().filter(voucher -> voucher.getStatus().name().equalsIgnoreCase(status)).collect(Collectors.toList());

        String returning = "";

        if (status.equalsIgnoreCase("New")){
            model.addAttribute("vouchers", vouchersFiltered);
            model.addAttribute("totalVoucher", vouchersFiltered.size());
            returning = "Admin/adminDashboard :: newVoucherFrag";
        }

        if (status.equalsIgnoreCase("Published")){
            model.addAttribute("vouchers", vouchersFiltered);
            model.addAttribute("totalVoucher", vouchersFiltered.size());
            returning = "Admin/adminDashboard :: publishedVoucherFrag";
        }

        return returning;
    }


    @GetMapping("/accountFragment")
    public String accountFrag(Model model){
        List<User> users = this.userService.findAll();

        model.addAttribute("users", users);
        model.addAttribute("totalUser", users.size());
        return "Admin/adminDashboard :: accountManagementFrag";
    }

    @GetMapping("/categoryFragment")
    public String categoryFrag(Model model){
        List<Category> categories = this.categoryService.findAll();

        List<Category> categoriesPreparing = categories.stream()
                .filter(category -> category.getStatus() == Category.Status.Pending)//Get all categories with the status is preparing
                .collect(Collectors.toList());

        model.addAttribute("categories", categoriesPreparing);
        return "Admin/adminDashboard :: categoryManagementFrag";
    }

    @GetMapping("/sellerFragment")
    public String sellerFrag(Model model){
        List<User> users = this.userService.findAll();

        List<User> sellers = users.stream().filter(user -> user.getRole().getName().equalsIgnoreCase("ROLE_SELLER")).collect(Collectors.toList());

        List<Map<String, Object>> profilesData = new ArrayList<>();

        sellers.forEach(user -> {
            Map<String, Object> profileData = this.userService.getProfileData(user);
            profilesData.add(profileData);
        });

        int totalUnitsSoldAllSeller = profilesData.stream().mapToInt(data -> (int) data.get("totalUnitsSold")).sum();

        model.addAttribute("totalUnitsSoldAllSeller", totalUnitsSoldAllSeller);
//        model.addAttribute("totalRevenueAllSeller", profilesData);
        model.addAttribute("profilesData", profilesData);
//        model.addAttribute("sellers", sellers);
        return "Admin/adminDashboard :: sellerFrag";
    }

    @GetMapping("/customerFragment")
    public String customerFrag(Model model){
        List<User> users = this.userService.findAll();

        List<User> customers = users.stream().filter(user -> user.getRole().getName().equalsIgnoreCase("ROLE_CUSTOMER")).collect(Collectors.toList());

        List<Map<String, Object>> profilesData = new ArrayList<>();

        customers.forEach(user -> {
            Map<String, Object> profileData = this.userService.getProfileData(user);
            profilesData.add(profileData);
        });

//        model.addAttribute("customers", customers);
        model.addAttribute("profilesData", profilesData);
        return "Admin/adminDashboard :: customerFrag";
    }

    @GetMapping ("/categoryStatisticFragment")
    public String categoryStatisticFrag(Model model){
        List<Category> categories = this.categoryService.findAll().stream().filter(category -> category.getStatus().equals(Category.Status.Approved)).collect(Collectors.toList());
        model.addAttribute("categories", categories);
        return "Admin/adminDashboard :: categoryFrag";
    }














}






























