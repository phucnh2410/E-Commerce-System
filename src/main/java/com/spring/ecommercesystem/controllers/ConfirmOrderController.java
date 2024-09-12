package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.OrderDetailService;
import com.spring.ecommercesystem.services.OrderService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import com.spring.ecommercesystem.temp.CartTemp;
import com.spring.ecommercesystem.temp.OrderTemp;
import com.spring.ecommercesystem.temp.UserCart;
import jakarta.websocket.server.PathParam;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class ConfirmOrderController {
    private final OrderService orderService;

    private final ProductService productService;

    private final OrderDetailService orderDetailService;

    private final UserService userService;

    @Autowired
    public ConfirmOrderController(OrderService orderService, ProductService productService, OrderDetailService orderDetailService, UserService userService) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderDetailService = orderDetailService;
        this.userService = userService;
    }

    private List<OrderTemp> sortOrder(List<Order> orders) {
        List<OrderTemp> orderTemps = new LinkedList<>();

        orders.forEach(order -> {
            OrderTemp orderTemp = new OrderTemp()
                    .setAddress(order.getAddress())
                    .setPaymentMethod(order.getPaymentMethod())
                    .setVoucher(order.getVoucher())
                    .setFinalTotal(order.getTotalAmount())
                    .setId(order.getId())
                    .setStatus(order.getStatus());

            //get Orderdetail to get Products and quantity each product
            List<Product> products = new ArrayList<>();
            order.getOrderDetails().forEach(orderDetail -> {
                Product product = orderDetail.getProduct();
                products.add(product);
            });

            //get Seller from List Product
            List<User> sellers = products.stream().map(product -> product.getUser()).distinct().collect(Collectors.toList());//Get and Removes duplicate sellers in the stream

            List<UserCart> userCarts = new ArrayList<>();

            List<CartTemp> cartTempsList = new ArrayList<>();

            order.getOrderDetails().forEach(orderDetail -> {
                CartTemp cartTemp = new CartTemp()
                        .setProduct(orderDetail.getProduct())
                        .setQuantity(orderDetail.getProductQuantity());
                cartTempsList.add(cartTemp);
            });

            sellers.forEach(seller -> {
                List<CartTemp> cartTemps = cartTempsList.stream().filter(cartTemp -> cartTemp.getProduct().getUser().equals(seller)).collect(Collectors.toList());

                UserCart userCart = new UserCart()
                        .setUser(seller)
                        .setCartTemps(cartTemps);

                userCarts.add(userCart);
            });

            //set List UserCart into OrderTemp
            orderTemp.setUserCarts(userCarts);

            orderTemps.addFirst(orderTemp);
        });

        return orderTemps;
    }

    @GetMapping("/success")
    public String successfulOrder(@PathParam("id") Long id, Model model) {
        model.addAttribute("orderId", id);
        return "OrderManagement/successfulOrdered";
    }

    @GetMapping("/list")
    public String showAllOrders(){
        return "OrderManagement/orderManagement";
    }

    @GetMapping("/pendingOrderFragment")
    public String showPreparingOrders(Model model){
        User currentUser = this.userService.getCurrentUser();

        List<Order> orders = currentUser.getOrders();
        List<Order> preparingOrders = orders.stream().filter(order -> order.getStatus() == Order.Status.pending_confirmation).collect(Collectors.toList());
        List<OrderTemp> orderTemps = sortOrder(preparingOrders);

//        model.addAttribute("orders", orders);
        model.addAttribute("orderTemps", orderTemps);
        return "OrderManagement/orderManagement :: pendingOrderFrag";
    }

    @GetMapping("/deliveringOrderFragment")
    public String showDeliveringOrders(Model model){
        User currentUser = this.userService.getCurrentUser();

        List<Order> orders = currentUser.getOrders();
        List<Order> deliveringOrders = orders.stream().filter(order -> order.getStatus() == Order.Status.delivering).collect(Collectors.toList());
        List<OrderTemp> orderTemps = sortOrder(deliveringOrders);

//        model.addAttribute("orders", orders);
        model.addAttribute("orderTemps", orderTemps);
        return "OrderManagement/orderManagement :: deliveringOrderFrag";
    }

    @GetMapping("/receivedOrderFragment")
    public String showReceivedOrders(Model model){
        User currentUser = this.userService.getCurrentUser();

        List<Order> orders = currentUser.getOrders();
        List<Order> receivedOrders = orders.stream().filter(order -> order.getStatus() == Order.Status.received).collect(Collectors.toList());
        List<OrderTemp> orderTemps = sortOrder(receivedOrders);

//        model.addAttribute("orders", orders);
        model.addAttribute("orderTemps", orderTemps);
        return "OrderManagement/orderManagement :: receivedOrderFrag";
    }

    @GetMapping("/canceledOrderFragment")
    public String showCanceledOrders(Model model){
        User currentUser = this.userService.getCurrentUser();

        List<Order> orders = currentUser.getOrders();
        List<Order> canceledOrders = orders.stream().filter(order -> order.getStatus() == Order.Status.canceled).collect(Collectors.toList());
        List<OrderTemp> orderTemps = sortOrder(canceledOrders);

//        model.addAttribute("orders", orders);
        model.addAttribute("orderTemps", orderTemps);
        return "OrderManagement/orderManagement :: canceledOrderFrag";
    }



    @GetMapping
    public String showOrderByOrderId(@PathParam("id") Long id, Model model){
        if (id ==null){
            System.out.println("Order id does not exist");
        }
        Order order = this.orderService.findById(id);

        List<OrderDetail> orderDetails = order.getOrderDetails();

        List<Product> products = new ArrayList<>();
        //Get each product from orderDetails
        orderDetails.forEach(orderDetail -> {
            products.add(orderDetail.getProduct());
        });

        //Get each seller from Product
        List<User> sellers = products.stream().map(product ->
                product.getUser())
                .distinct() //distinct(): Removes duplicate elements in the stream
                .collect(Collectors.toList());

        //Create UserCart and add CartTemp
        List<UserCart> userCarts = new ArrayList<>();

        List<CartTemp> cartTempList = new ArrayList<>();
        orderDetails.forEach(orderDetail -> {
            CartTemp cartTemp = new CartTemp()
                    .setProduct(orderDetail.getProduct())
                    .setQuantity(orderDetail.getProductQuantity());

            cartTempList.add(cartTemp);
        });

        sellers.forEach(seller -> {
            List<CartTemp> cartTemps = cartTempList.stream()
                    .filter(cartTemp -> cartTemp.getProduct().getUser().equals(seller))
                    .collect(Collectors.toList());
            //
            UserCart userCart = new UserCart()
                    .setUser(seller)
                    .setCartTemps(cartTemps);
            userCarts.add(userCart);
        });

        Double productTotal = 0.0;
        for (CartTemp cartTemp:cartTempList){
            productTotal += (cartTemp.getProduct().getPrice() * cartTemp.getQuantity());
        }

        Double priceIsReduced = (order.getVoucher() != null) ? (productTotal * order.getVoucher().getPercentageIsReduced())/100 : 0;

        model.addAttribute("userCarts", userCarts);
        model.addAttribute("order", order);
        model.addAttribute("productTotal", productTotal);
        model.addAttribute("priceIsReduced", priceIsReduced);
        return "OrderManagement/orderPage";
    }























}
