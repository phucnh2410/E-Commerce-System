package com.spring.ecommercesystem.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.Voucher;
import com.spring.ecommercesystem.services.*;
import com.spring.ecommercesystem.temp.OrderTemp;
import com.spring.ecommercesystem.temp.UserCart;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    private final PaypalService paypalService;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final CartService cartService;
    private final VoucherService voucherService;

    @Autowired
    public PaymentController(PaypalService paypalService, UserService userService, OrderService orderService, OrderDetailService orderDetailService, CartService cartService, VoucherService voucherService) {
        this.paypalService = paypalService;
        this.userService = userService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.cartService = cartService;
        this.voucherService = voucherService;
    }

    @GetMapping("/paypal")
    public String paypalHome() {
        return "Payment/paypal";
    }


    @PostMapping("/paypal/create")
    public String createPayment(@RequestParam("total") Double total) {
        // total,
        try{
            String cancelUrl = "http://localhost:8080/shopping_cart/checkout";
            String successUrl = "http://localhost:8080/payment/paypal/success";

            Payment payment = paypalService.createPayment(total, "USD", "paypal", "sale", "Payment Description", cancelUrl, successUrl);

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e){
            log.error("Error occurred:", e);
        }

        return "redirect:/payment/paypal/error";
    }

    @GetMapping("/paypal/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, Model model, HttpSession session) {
        try{
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {

                OrderTemp orderTempSession = (OrderTemp) session.getAttribute("orderTemp");
                List<UserCart> defectiveUserCarts = new ArrayList<>();

                orderTempSession.getUserCarts().stream().forEach(userCart -> {
                    defectiveUserCarts.add(userCart);
                });

                Voucher voucher = ( (orderTempSession.getVoucher() != null) && !(orderTempSession.getVoucher().equals("")) && (orderTempSession.getVoucher().getId() != null)) ? orderTempSession.getVoucher() : null;

    //            //Set an order
                Order order = new Order()
                        .setAddress(orderTempSession.getAddress())
                        .setPaymentMethod(orderTempSession.getPaymentMethod())
                        .setVoucher(voucher)
                        .setUser(this.userService.getCurrentUser())
                        .setTotalAmount(orderTempSession.getFinalTotal())
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

                //Delete data in the session when user ordered successful.
                session.removeAttribute("orderTemp");
                session.removeAttribute("userCarts");

                model.addAttribute("paymentId", paymentId);
                model.addAttribute("orderId", order.getId());
                return "Payment/paymentSuccess";
            }
        }catch (PayPalRESTException e){
            log.error("Error occurred:", e);
        }
        return "Payment/paymentSuccess";
    }

//    @GetMapping("/paypal/cancel")
//    public String paymentCancel() {
//        return "Payment/paymentCanceled";
//    }

    @GetMapping("/paypal/error")
    public String paymentError() {
        return "Payment/paymentFailed";
    }

}
