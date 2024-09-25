package com.spring.ecommercesystem.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommercesystem.services.*;
import com.spring.ecommercesystem.temp.CartTemp;
import com.spring.ecommercesystem.temp.UserCart;
import com.spring.ecommercesystem.entities.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shopping_cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final PaymentMethodService paymentService;

    private final UserService userService;
    private final AddressService addressService;
    private final VoucherService voucherService;

    @Autowired
    public CartController(CartService cartService, ProductService productService, PaymentMethodService paymentService, UserService userService, AddressService addressService, VoucherService voucherService) {
        this.cartService = cartService;
        this.productService = productService;
        this.paymentService = paymentService;
        this.userService = userService;
        this.addressService = addressService;
        this.voucherService = voucherService;
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
                                .getUser()
                ).distinct() //distinct(): Removes duplicate elements in the stream
                .collect(Collectors.toList());

        //Get product for each user
        List<UserCart> userCarts = new ArrayList<>();
        users.forEach(user -> {
            List<CartTemp> cartTemps = cartTempList.stream()
                    .filter(cartTemp -> cartTemp.getProduct().getUser().equals(user))
                    .collect(Collectors.toList());

            UserCart userCart = new UserCart(user, cartTemps);
            userCarts.add(userCart);
        });

//        double totalMoney = this.cartService.getTotalAllItems();
//        Long numberOfItem = this.cartService.getTotalItemsInCart();

        model.addAttribute("userCarts", userCarts);
//        model.addAttribute("total", totalMoney);
//        model.addAttribute("totalNumberOfProduct", numberOfItem);
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
//        model.addAttribute("total", this.cartService.getTotalAllItems());
//        model.addAttribute("totalNumberOfProduct", this.cartService.getTotalItemsInCart());
        return "ShoppingCart/shoppingCart :: sellerFrag";
    }


    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        //get all UserCart from session
        List<UserCart> userCarts = (List<UserCart>) session.getAttribute("userCarts");

        Double totalAmount = userCarts.stream()
                .flatMap(userCart -> userCart.getCartTemps().stream())//Get all cartTemps from all userCarts
                .mapToDouble(CartTemp::getTotal).sum();//get total value for each cartTemp and sum them.


            model.addAttribute("addresses", this.addressService.findAll());
            model.addAttribute("vouchers", this.userService.getCurrentUser().getVoucherDetails().stream().filter(vc -> vc.getStatus().equals(VoucherDetail.Status.Unused)));
            model.addAttribute("paymentMethods", this.paymentService.findAll());
            model.addAttribute("default_address", this.addressService.findAll().stream().filter(Address::isStatus).findFirst().get() );
            model.addAttribute("userCarts", userCarts);
            model.addAttribute("totalAmount", totalAmount);
        return "Checkout/checkoutPage";
    }

    @GetMapping("/address_fragment/{id}")
    public String showAddressById(@PathVariable Long id, Model model){
        model.addAttribute("default_address", this.addressService.findById(id));

        return  "Checkout/checkoutPage :: addressFrag";
    }

}




















