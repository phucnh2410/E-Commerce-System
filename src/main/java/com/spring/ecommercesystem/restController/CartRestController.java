package com.spring.ecommercesystem.restController;

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

import java.util.Collection;
import java.util.UUID;

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
        System.out.println(this.cartService.getCart());
        return ResponseEntity.ok().body("Product added to cart successfully");
    }
}













