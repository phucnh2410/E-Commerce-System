package com.spring.ecommercesystem.restController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import com.spring.ecommercesystem.temp.CartTemp;
import com.spring.ecommercesystem.temp.UserCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutRestController {
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public CheckoutRestController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }


    @PostMapping()
    public ResponseEntity<Map<String, Object>> getCheckout(@RequestBody List<Map<String, Object>> data){
        Map<String, Object> response = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try{
            //convert Data from JSON to Object
            List<UserCart> userCartsResponse = mapper.convertValue(data, new TypeReference<List<UserCart>>() {});

            List<UserCart> userCarts = new ArrayList<>();

            userCartsResponse.stream().forEach(userCart -> {
                //Get Useer from the UserCart response
                User user = this.userService.findById(userCart.getUser().getId());
                List<CartTemp> cartTemps = new ArrayList<>();

                userCart.getCartTemps().stream().forEach(cartTemp -> {
                    //Get product from the cartTemp response
                    Product product = this.productService.findById(cartTemp.getProduct().getId());
                    int quantity = cartTemp.getQuantity();
                    Double total = cartTemp.getTotal();
                    //Set Product and ... into CartTemp
                    CartTemp cartTempObject = new CartTemp()
                            .setProduct(product)
                            .setQuantity(quantity)
                            .setTotal(total);
                    //Add CartTemp Object into List
                    cartTemps.add(cartTempObject);
                });

                //set User and CartTemp into UserCart
                UserCart userCartObject = new UserCart()
                        .setUser(user)
                        .setCartTemps(cartTemps);
                //Add UserCart into List
                userCarts.add(userCartObject);
            });

            response.put("userCarts", userCarts);
            response.put("message", "Get user cart successfully!!!");
        }catch (Exception e){
            response.put("message", "Error processing user cart data.");
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok().body(response);
    }
}
