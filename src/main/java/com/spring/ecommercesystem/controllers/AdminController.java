package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Category;
import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CategoryService;
import com.spring.ecommercesystem.services.OrderService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import com.spring.ecommercesystem.temp.CartTemp;
import com.spring.ecommercesystem.temp.OrderTemp;
import com.spring.ecommercesystem.temp.UserCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final OrderService orderService;

    private final UserService userService;

    private final CategoryService categoryService;

    private final ProductService productService;

    @Autowired
    public AdminController(OrderService orderService, UserService userService, CategoryService categoryService, ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
    }


    @GetMapping
    public String showAdminPage(Model model){
        return "Admin/adminDashboard";
    }


    @GetMapping("/orderFragment")
    public String orderFrag (Model model){
        List<Order> orders = this.orderService.findAll();
        model.addAttribute("orders", orders);
        return "Admin/adminDashboard :: orderManagementFrag";
    }


    @GetMapping("/accountFragment")
    public String accountFrag(Model model){
        List<User> users = this.userService.findAll();

        model.addAttribute("users", users);
        return "Admin/adminDashboard :: accountManagementFrag";
    }

    @GetMapping("/categoryFragment")
    public String categoryFrag(Model model){
        List<Category> categories = this.categoryService.findAll();

        model.addAttribute("categories", categories);
        return "Admin/adminDashboard :: categoryManagementFrag";
    }

    @GetMapping("/sellerFragment")
    public String sellerFrag(Model model){
        List<User> users = this.userService.findAll();

        List<User> sellers = new ArrayList<>();

        users.forEach(user -> {
            boolean isSeller = user.getRole().getName().equals("ROLE_SELLER");
            if (isSeller){
                sellers.add(user);
            }
        });

        model.addAttribute("sellers", sellers);
        return "Admin/adminDashboard :: sellerFrag";
    }

    @GetMapping("/customerFragment")
    public String customerFrag(Model model){
        List<User> users = this.userService.findAll();

        List<User> customers = new ArrayList<>();

        users.forEach(user -> {
            boolean isCustomer = user.getRole().getName().equals("ROLE_CUSTOMER");
            if (isCustomer){
                customers.add(user);
            }
        });

        model.addAttribute("customers", customers);
        return "Admin/adminDashboard :: customerFrag";
    }

    @GetMapping ("/categoryStatisticFragment")
    public String categoryStatisticFrag(Model model){
        List<Category> categories = this.categoryService.findAll();
        model.addAttribute("categories", categories);
        return "Admin/adminDashboard :: categoryFrag";
    }














}






























