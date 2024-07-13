package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Cart;
import com.spring.ecommercesystem.services.CartService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cart")
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
    public ResponseEntity<Map<String, Object>> updateQuantity(@PathVariable("productId") Long productId, @RequestParam("quantity") int quantity){
        Map<String, Object> response = new HashMap<>();
        try {
            this.cartService.updateQuantity(productId, quantity);
            Cart cart = this.cartService.getCartByProductId(productId);

            response.put("quantity", cart.getQuantity());
            response.put("price", cart.getProduct().getPrice());
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/add/{id}")
    public ResponseEntity<String> addProductToCart(@PathVariable("id") Long productId, @RequestParam("quantity") int quantity){
        //Get cart id to check cart existed
        String cartId = this.cartService.getCartId();
        if (cartId == null){
            //Create Cart with an ID
            this.cartService.createCartId();
        }

        //Add product to cart
        this.cartService.addProductToCart(productId, quantity);
        return ResponseEntity.ok().body("Product added to cart successfully");
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
}













