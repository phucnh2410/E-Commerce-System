package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Address;
import com.spring.ecommercesystem.entities.Cart;
import com.spring.ecommercesystem.entities.PaymentMethod;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CartService;
import com.spring.ecommercesystem.services.PaymentMethodService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/shopping_cart")
public class CartController {
    private final CartService cartService;
    private final PaymentMethodService paymentService;

    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, PaymentMethodService paymentService, UserService userService) {
        this.cartService = cartService;
        this.paymentService = paymentService;
        this.userService = userService;
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

    @GetMapping("/fragment/{productId}")
    public String getCartFragmentByProductId(@PathVariable("productId") Long productId, Model model){
        Cart cart = this.cartService.getCartByProductId(productId);

        model.addAttribute("cartItems",cart);
        return "ShoppingCart/shoppingCart :: cartItemFrag";
    }


    @GetMapping("/checkout")
    public String showCheckout(Model model){
        User currentUser = this.userService.getCurrentUser();
        List<Address> addresses = currentUser.getAddresses();
        List<PaymentMethod> paymentMethods = this.paymentService.findAll();

        model.addAttribute("addresses", addresses);
        model.addAttribute("payments", paymentMethods);
        return "Checkout/checkoutPage";
    }
}




















