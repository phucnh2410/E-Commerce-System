package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.temp.CartTemp;
import com.spring.ecommercesystem.temp.UserCart;
import com.spring.ecommercesystem.entities.*;
import com.spring.ecommercesystem.services.CartService;
import com.spring.ecommercesystem.services.PaymentMethodService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shopping_cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final PaymentMethodService paymentService;

    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, ProductService productService, PaymentMethodService paymentService, UserService userService) {
        this.cartService = cartService;
        this.productService = productService;
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @GetMapping
    public String showShoppingCart(Model model){
        List<Cart> cartItems = this.cartService.getCart();
        //Transfer data object
        List<CartTemp> cartTempList = new ArrayList<>();

        cartItems.stream().forEach(cart -> {
            try {
                Product product = this.productService.findById(cart.getProduct().getId());

                CartTemp cartTemp = new CartTemp()
                        .setProduct(product)
                        .setQuantity(cart.getQuantity());
                cartTempList.add(cartTemp);
            }catch (NoSuchElementException e) {
                // Xử lý khi sản phẩm không tồn tại
                System.out.println("Product not found: ");
            }
        });

        //Get user list from already product in the cart
        List<User> users = cartTempList.stream().map(cartTemp ->
                        cartTemp
                                .getProduct()
                                .getUser()).distinct().collect(Collectors.toList());

        //Get product for each user
        List<UserCart> userCarts = new ArrayList<>();
        users.forEach(user -> {
            List<CartTemp> cartTemps = cartTempList.stream()
                    .filter(cartTemp -> cartTemp.getProduct().getUser().equals(user))
                    .collect(Collectors.toList());

            UserCart userCart = new UserCart(user, cartTemps);
            userCarts.add(userCart);
        });

        double totalMoney = this.cartService.getTotalAllItems();
        Long numberOfItem = this.cartService.getTotalItemsInCart();

        model.addAttribute("userCarts", userCarts);
        model.addAttribute("total", totalMoney);
        model.addAttribute("totalNumberOfProduct", numberOfItem);
        return "ShoppingCart/shoppingCart";
    }

    @GetMapping("/seller_fragment")
    public String getFragment(Model model){

        //If the user has no products left in the cart, remove the user from the cart.
        List<Cart> cartItems = this.cartService.getCart();
        //Transfer data object
        List<CartTemp> cartTempList = new ArrayList<>();

        cartItems.stream().forEach(cart -> {
            Product product = this.productService.findById(cart.getProduct().getId());

            CartTemp cartTemp = new CartTemp()
                    .setProduct(product)
                    .setQuantity(cart.getQuantity());
            cartTempList.add(cartTemp);
        });

        //Get user list from already product in the cart
        List<User> users = cartTempList.stream().map(cartTemp ->
                cartTemp
                        .getProduct()
                        .getUser()).distinct().collect(Collectors.toList());

        //Get product for each user
        List<UserCart> userCarts = new ArrayList<>();
        users.forEach(user -> {
            List<CartTemp> cartTemps = cartTempList.stream()
                    .filter(cartTemp -> cartTemp.getProduct().getUser().equals(user))
                    .collect(Collectors.toList());

            UserCart userCart = new UserCart(user, cartTemps);
            userCarts.add(userCart);
        });

        model.addAttribute("userCarts", userCarts);
        model.addAttribute("total", this.cartService.getTotalAllItems());
        model.addAttribute("totalNumberOfProduct", this.cartService.getTotalItemsInCart());
        return "ShoppingCart/shoppingCart :: sellerFrag";
    }


    @GetMapping("/checkout")
    public String showCheckout(Model model){
//        List<Cart> cartList = this.cartService.getCart();
//        //Transfer data object
//        List<CartTemp> cartTempList = new ArrayList<>();
//
//        cartList.stream().forEach(cart -> {
//            Product product = this.productService.findById(cart.getProduct().getId());
//
//            CartTemp cartTemp = new CartTemp()
//                    .setProduct(product)
//                    .setUser(product.getUser())
//                    .setQuantity(cart.getQuantity());
//
//            cartTempList.add(cartTemp);
//        });

        User currentUser = this.userService.getCurrentUser();
        List<Address> addresses = currentUser.getAddresses();
        List<PaymentMethod> paymentMethods = this.paymentService.findAll();

//        model.addAttribute("addresses", addresses);
//        model.addAttribute("cartList", cartTempList);
//        model.addAttribute("payments", paymentMethods);
//        System.out.println("Cart list: "+cartTempList);
        return "Checkout/checkoutPage";
    }
}




















