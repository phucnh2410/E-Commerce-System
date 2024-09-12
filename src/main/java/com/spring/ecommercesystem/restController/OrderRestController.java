package com.spring.ecommercesystem.restController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.Voucher;
import com.spring.ecommercesystem.services.*;
import com.spring.ecommercesystem.temp.OrderTemp;
import com.spring.ecommercesystem.temp.UserCart;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    @GetMapping("/repurchase_order/{id}")
    public ResponseEntity<List<Long>> repurchaseOrder (@PathVariable Long id){
        Order order = this.orderService.findById(id);
        List<OrderDetail> orderDetails = order.getOrderDetails();

        List<Long> productIds = orderDetails.stream().map(orderDetail -> orderDetail.getProduct().getId()).collect(Collectors.toList());
        return ResponseEntity.ok().body(productIds);
    }


    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveOrder (@RequestBody String data, HttpSession session){
        Map<String, Object> response = new HashMap<>();

        if (data == null){
            response.put("message", "Some thing went wrong, Please try again!!!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        ObjectMapper mapper = new ObjectMapper();

        try{
            OrderTemp orderTempResponse = mapper.readValue(data, new TypeReference<OrderTemp>() {});

            List<UserCart> defectiveUserCarts = new ArrayList<>();

            orderTempResponse.getUserCarts().stream().forEach(userCart -> {
                defectiveUserCarts.add(userCart);
            });

            Voucher voucher = ( (orderTempResponse.getVoucher() != null) && !(orderTempResponse.getVoucher().equals("")) && (orderTempResponse.getVoucher().getId() != null)) ? orderTempResponse.getVoucher() : null;

//            //Set an order
            Order order = new Order()
                    .setAddress(orderTempResponse.getAddress())
                    .setPaymentMethod(orderTempResponse.getPaymentMethod())
                    .setVoucher(voucher)
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

            //Delete data in the session when user ordered successful.
            session.removeAttribute("userCarts");

        }catch (Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/store_order_info")
    public ResponseEntity<String> storeOrderToPayment (@RequestBody String data, HttpSession session){

        if (data == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some thing went wrong, Please try again!!!");
        }

        ObjectMapper mapper = new ObjectMapper();
        try{
            OrderTemp orderTempResponse = mapper.readValue(data, new TypeReference<OrderTemp>() {});

            session.setAttribute("orderTemp", orderTempResponse);
            return ResponseEntity.status(HttpStatus.OK).body("Order info stored successfully!!!");
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("orderTemp", new OrderTemp());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process checkout data");
        }
    }


    @PutMapping("/update_status/{id}/{status}")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable("id") Long id, @PathVariable String status){
        Map<String, Object> response = new HashMap<>();

        Order order = this.orderService.findById(id);
        if ( (order == null) || (status == null) ){
            response.put("message", "This order may have been previously cancelled, Please try again later!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (status.equals("cancel")){
            order.setStatus(Order.Status.canceled);
            order.setCanceledDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
            response.put("message", "This order has been cancelled successfully");
        }else if (status.equals("confirm")){
            order.setStatus(Order.Status.confirmed);
            order.setConfirmedDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
            response.put("message", "This order has been confirmed");
        }else if (status.equals("delivering")){
            order.setStatus(Order.Status.delivering);
            order.setDeliveredDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
            response.put("message", "Order status updated to delivering");
        }else if (status.equals("received")){
            order.setStatus(Order.Status.received);
            order.setReceivedDate(Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
            //Update stock when the order was received
            List<OrderDetail> items = order.getOrderDetails();
            items.stream().forEach(item -> {
                Long productId = item.getProduct().getId();
                int quantity = item.getProductQuantity();
                Product product = this.productService.findById(productId);
                product.setStock(product.getStock() - quantity);
                this.productService.saveAndUpdate(product);
            });
            response.put("message", "Order has been received successfully");
        }

        this.orderService.saveAndUpdate(order);
        response.put("order", order);


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }






}
