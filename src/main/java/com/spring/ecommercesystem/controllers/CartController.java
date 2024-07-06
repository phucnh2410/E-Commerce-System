package com.spring.ecommercesystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("shopping_cart")
public class CartController {
    @GetMapping
    public String showShoppingCart(Model model){
        return "ShoppingCart/shoppingCart";
    }
}
