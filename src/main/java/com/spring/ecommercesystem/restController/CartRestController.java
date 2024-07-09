package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Cart;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CartService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PostMapping("/add/{id}")
    public ResponseEntity<String> addProductToCart(@PathVariable("id") Long productId, @RequestParam("quantity") int quantity){
        //Create Cart with an ID
        String cartId = this.cartService.createCartId();
        //Get product by ID
        Product product = this.productService.findById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        //Add product to cart
        this.cartService.addProductToCart(product, quantity);
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













