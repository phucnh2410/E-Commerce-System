package com.spring.ecommercesystem.restController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.services.*;
import com.spring.ecommercesystem.temp.OrderTemp;
import com.spring.ecommercesystem.temp.UserCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {
    private final OrderService orderService;

    private final AddressService addressService;

    private final PaymentMethodService paymentService;

    private final VoucherService voucherService;

    private final UserService userService;

    private final ProductService productService;

    private final OrderDetailService orderDetailService;

    private final CartService cartService;

    @Autowired
    public OrderRestController(OrderService orderService, AddressService addressService, PaymentMethodService paymentService, VoucherService voucherService, UserService userService, ProductService productService, OrderDetailService orderDetailService, CartService cartService) {
        this.orderService = orderService;
        this.addressService = addressService;
        this.paymentService = paymentService;
        this.voucherService = voucherService;
        this.userService = userService;
        this.productService = productService;
        this.orderDetailService = orderDetailService;
        this.cartService = cartService;
    }


    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveOrder (@RequestBody String data){
        Map<String, Object> response = new HashMap<>();

        if (data == null){
            response.put("message", "get data is null!!!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        ObjectMapper mapper = new ObjectMapper();

        try{
            OrderTemp orderTempResponse = mapper.readValue(data, new TypeReference<OrderTemp>() {});

            List<UserCart> defectiveUserCarts = new ArrayList<>();

            orderTempResponse.getUserCarts().stream().forEach(userCart -> {
                defectiveUserCarts.add(userCart);
            });

//            //Set an order
            Order order = new Order()
                    .setAddress(orderTempResponse.getAddress())
                    .setPaymentMethod(orderTempResponse.getPaymentMethod())
                    .setVoucher(orderTempResponse.getVoucher())
                    .setUser(this.userService.getCurrentUser())
                    .setTotalAmount(orderTempResponse.getFinalTotal())
                    .setOrderedDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())))
                    .setStatus(Order.Status.pending_confirmation);
            //Save an order
            this.orderService.saveAndUpdate(order);


            //Create list of orderDetail
            List<OrderDetail> orderDetails = new ArrayList<>();
            //Loop through each cartTemp to get product and quantity to set it into each orderDetail
            defectiveUserCarts.stream().forEach(userCart -> {
                userCart.getCartTemps().stream().forEach(cartTemp -> {
                    OrderDetail orderDetail = this.orderDetailService.addProductToOrder(order, cartTemp.getProduct(), cartTemp.getQuantity());
                    orderDetails.add(orderDetail);
                });
            });

            order.setOrderDetails(orderDetails);
            this.orderService.saveAndUpdate(order);

            //Handle cart after ordered successful
            if (orderDetails.size() == this.cartService.getTotalItemsInCart()){
                this.cartService.cleanCart();
            }

            if (orderDetails.size() != this.cartService.getTotalItemsInCart()){
                defectiveUserCarts.stream().forEach(userCart -> {
                        userCart.getCartTemps().stream().forEach(cartTemp -> {
                            Long productId = cartTemp.getProduct().getId();
                            this.cartService.removeProductFromCart(productId);
                        });
                    }
                );
            }

            response.put("order", order);
            response.put("address", this.addressService.findById(orderTempResponse.getAddress().getId()));
            response.put("payment", this.paymentService.findById(orderTempResponse.getPaymentMethod().getId()));
            response.put("message", "You ordered successful!!!");

        }catch (Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(response);

    }






}
