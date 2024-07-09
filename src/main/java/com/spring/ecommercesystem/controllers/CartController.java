package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Cart;
import com.spring.ecommercesystem.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/shopping_cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String showShoppingCart(Model model){
        List<Cart> cartItems = this.cartService.getCart();
        double totalMoney = this.cartService.getTotalAllItems();
        Long numberOfItem = this.cartService.getTotalItemsInCart();
//
//        cartItems.stream().forEach(cart -> {
//            System.out.println("Product id: "+cart.getProduct().getId()+" Name: "+cart.getProduct().getName() +" Quantity: "+cart.getQuantity());
//        });
//
//        System.out.println("Number of product in Cart: "+numberOfItem);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", totalMoney);
        model.addAttribute("totalNumberOfProduct", numberOfItem);
        return "ShoppingCart/shoppingCart";
    }

    @GetMapping("/fragment")
    public String getFragment(Model model){
        double totalMoney = this.cartService.getTotalAllItems();
        Long numberOfItem = this.cartService.getTotalItemsInCart();

        model.addAttribute("cartItems", this.cartService.getCart());
        model.addAttribute("total", totalMoney);
        model.addAttribute("totalNumberOfProduct", numberOfItem);
        return "ShoppingCart/shoppingCart :: cartBodyFrag";
    }
}
