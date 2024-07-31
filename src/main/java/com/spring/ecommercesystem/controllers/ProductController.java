package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping()
    public String allProducts(Model model){
        User currentUser = this.userService.getCurrentUser();
        List<Product> products = currentUser.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("user", currentUser);

        return "Product/productManagement";
    }

    @GetMapping("/fragment")
    public String productFragment(Model model){
        User currentUser = this.userService.getCurrentUser();
        List<Product> products = currentUser.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("user", currentUser);

        return "Product/productManagement :: productManagementFrag";
    }

    @GetMapping("/search")
    public String productSearch(@RequestParam("name") String name, Model model){
        List<Product> products = this.productService.findByName(name);
        model.addAttribute("products", products);
        return "Product/productSearching";
    }
}




















