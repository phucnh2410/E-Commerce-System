package com.spring.ecommercesystem.restController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommercesystem.entities.Cart;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CartService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import com.spring.ecommercesystem.temp.CartTemp;
import com.spring.ecommercesystem.temp.UserCart;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cart")
@Slf4j
public class CartRestController {
    private final CartService cartService;
    private final UserService userService;

    private final ProductService productService;

    @Autowired
    public CartRestController(CartService cartService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/getCart")
    public ResponseEntity<Map<String, Object>> getCartDetail(){
        Map<String, Object> response = new HashMap<>();

        double totalMoney = this.cartService.getTotalAllItems();
        Long numberOfItem = this.cartService.getTotalItemsInCart();
        List<Cart> cartItems = this.cartService.getCart();

        response.put("totalMoney", totalMoney);
        response.put("numberOfItem", numberOfItem);
        response.put("cartItems", cartItems);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/number")
    public ResponseEntity<Long> showNumberOfProduct(){
        Long totalNumberOfProduct = this.cartService.getTotalItemsInCart();
        if (totalNumberOfProduct == null){
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(totalNumberOfProduct);
    }

    @PostMapping("/quantity/{productId}")
    public ResponseEntity<Map<String, Object>> updateQuantity(@PathVariable("productId") Long productIdRequest, @RequestParam("quantity") int quantityRequest){
        Map<String, Object> response = new HashMap<>();

        try {
            int stock = this.productService.findById(productIdRequest).getStock();
            if (quantityRequest <= stock) {
                this.cartService.updateQuantity(productIdRequest, quantityRequest);
                Cart cart = this.cartService.getCartByProductId(productIdRequest);

                response.put("message", null);
                response.put("quantity", cart.getQuantity());
                response.put("price", cart.getProduct().getPrice());
                return ResponseEntity.ok().body(response);
            }

            response.put("stock", stock);
            response.put("message", "The number of products has reached the limit");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/add/{id}")
    public ResponseEntity<Map<String, Object>> addProductToCart(@PathVariable("id") Long productIdRequest, @RequestParam("quantity") int quantityRequest){
        Map<String, Object> response = new HashMap<>();

        try {
            int productStock = this.productService.findById(productIdRequest).getStock();
            if (quantityRequest <= productStock) {
                Cart cart = this.cartService.getCartByProductId(productIdRequest);

                if (cart == null){
                    response.put("message", "Product added to cart successfully");
                    //Add product to cart
                    this.cartService.addProductToCart(productIdRequest, quantityRequest);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }

                int quantityInCart = cart.getQuantity();
                // Kiểm tra nếu số lượng trong giỏ đã đạt tới giới hạn
                if ( (quantityInCart == productStock) || (quantityInCart > productStock)) {
                    response.put("message", "The number of products in the cart has reached the limit");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }

                int maximumQuantity = productStock - quantityInCart;

                // Kiểm tra có thể thêm được sản phẩm nhưng yêu cầu về số lượng sp để thêm đã đạt tới giới hạn
                if (quantityRequest > maximumQuantity){
                    response.put("message", "You can only add a maximum of "+maximumQuantity+" more products to your cart.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }

                //Add product to cart
                response.put("message", "Product added to cart successfully");
                this.cartService.addProductToCart(productIdRequest, quantityRequest);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

            response.put("message", "The requested product quantity exceeds the available stock.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable("id")  Long id){
        try{
            this.cartService.removeProductFromCart(id);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/store_checkout_info")
    public ResponseEntity<String> storeDataToCheckout(@RequestBody String data, HttpSession session) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            List<UserCart> userCartsResponse = mapper.readValue(data, new TypeReference<List<UserCart>>() {});
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

            //Store the checkout info data into Session
            session.setAttribute("userCarts", userCarts);

            return ResponseEntity.ok().body("Checkout info stored successful!!!");
        }catch (JsonProcessingException e){
            log.error("Error occurred:", e);
            // Optionally re-try parsing or fall back to a default value
            session.setAttribute("userCarts", new ArrayList<>()); // Fallback to an empty list
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process checkout data");
        }
    }
}













