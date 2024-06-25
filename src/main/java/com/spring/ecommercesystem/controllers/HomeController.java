package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CategoryService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.service.annotation.GetExchange;

import javax.swing.*;

@Controller
public class HomeController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;


    @Autowired
    public HomeController(ProductService productService, UserService userService, CategoryService categoryService) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @GetMapping("/home")
    public String home(){
        return "Home/homePage";
    }

    @GetMapping("/login")
    public String login(){

        return "Home/loginAndRegister";
    }

    @GetMapping("/profile")
    public String profile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = this.userService.findByEmail(email);

        model.addAttribute("user", user);
        return "Home/profile";
    }

}














