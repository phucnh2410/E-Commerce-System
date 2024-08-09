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

//        List<OrderTemp> orderTemps = new ArrayList<>();
//
//        orders.forEach(order -> {
//            OrderTemp orderTemp = new OrderTemp()
//                    .setAddress(order.getAddress())
//                    .setPaymentMethod(order.getPaymentMethod())
//                    .setVoucher(order.getVoucher())
//                    .setFinalTotal(order.getTotalAmount())
//                    .setId(order.getId())
//                    .setStatus(order.getStatus());
//
//            //get Orderdetail to get Products and quantity each product
//            List<Product> products = new ArrayList<>();
//            order.getOrderDetails().forEach(orderDetail -> {
//                Product product = orderDetail.getProduct();
//                products.add(product);
//            });
//
//            //get Seller from List Product
//            List<User> sellers = products.stream().map(product -> product.getUser()).distinct().collect(Collectors.toList());//Get and Removes duplicate sellers in the stream
//
//            List<UserCart> userCarts = new ArrayList<>();
//
//            List<CartTemp> cartTempsList = new ArrayList<>();
//
//            order.getOrderDetails().forEach(orderDetail -> {
//                CartTemp cartTemp = new CartTemp()
//                        .setProduct(orderDetail.getProduct())
//                        .setQuantity(orderDetail.getProductQuantity());
//                cartTempsList.add(cartTemp);
//            });
//
//            sellers.forEach(seller -> {
//                List<CartTemp> cartTemps = cartTempsList.stream().filter(cartTemp -> cartTemp.getProduct().getUser().equals(seller)).collect(Collectors.toList());
//
//                UserCart userCart = new UserCart()
//                        .setUser(seller)
//                        .setCartTemps(cartTemps);
//
//                userCarts.add(userCart);
//            });
//
//            //set List UserCart into OrderTemp
//            orderTemp.setUserCarts(userCarts);
//
//            orderTemps.add(orderTemp);
//        });

//        model.addAttribute("orderTemps", orderTemps);
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














}






























