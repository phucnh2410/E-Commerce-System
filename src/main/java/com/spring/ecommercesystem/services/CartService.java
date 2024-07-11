package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Cart;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;

    private final ProductService productService;
    private static final String CART_PREFIX = "cart:";

    @Autowired
    public CartService(RedisTemplate<String, Object> redisTemplate, UserService userService, ProductService productService) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
        this.productService = productService;
    }

    public Long getUserIdAuthenticate (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("User is not authenticated!");
            return null;
        }
        UserDetails userAuth = (UserDetails) authentication.getPrincipal();
        if (userAuth == null){
            System.out.println("User does not authenticate!!!");
            return null;
        }
        Collection<? extends GrantedAuthority> authorities = userAuth.getAuthorities();

        boolean isCustomer = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"));
        if (isCustomer){
            return this.userService.getCurrentUser().getId();
        }
        return null;
    }


    //Create a shopping cart with an ID
    public String createCartId(){
        Long userId = getUserIdAuthenticate();
        if (userId == null){
            System.out.println("User does not login or not permission to access this function");
            return null;
        }

        //Make user id to become cart id
        String cartId = String.valueOf(userId);
        String key  = CART_PREFIX + cartId;

        //Store the cart id inti Redis cache
        redisTemplate.opsForHash().put(key, "cartId", cartId);
        return cartId;
    }

    public String getCartId(){
        Long userId = getUserIdAuthenticate();
        if (userId == null){
            System.out.println("User does not login or not permission to access this function");
            return null;
        }
        //Using the user id to get a cart id
        String key  = CART_PREFIX + userId;

        //Get a cart id from Redis cache
        String cartId = (String) redisTemplate.opsForHash().get(key, "cartId");
        return cartId;
    }

    public String getKey(){
        String cartId = getCartId();
        if (cartId == null){
            throw new IllegalStateException("Cart ID is null in the addProductToCart() method");
        }
        String key = CART_PREFIX + cartId;

        return key;
    }

    //Get the number of already products in the cart of the user
    public Long getTotalItemsInCart(){
        String key = getKey();

        //Get cart from the Redis cache
        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");
        if (cartItems == null){
            return 0L;
        }

        //Get the total of already products in the cart
        return (long) cartItems.size();
    }

    public void addProductToCart(Long productId, int quantity){
        //get Product from DB
        Product product = this.productService.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found for id: " + productId);
        }

        String key = getKey();
        //Get cart from the Redis cache
        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");

        if (cartItems == null){
            cartItems = new HashMap<>();
        }

        //Check if the product exists in the shopping cart?
        Cart cart = cartItems.get(productId);
        if (cart == null){
            cart = new Cart();
            cart.setProduct(product);
            cart.setSellerName(product.getUser().getFullName());
            cart.setQuantity(quantity);
        }else {
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        cartItems.put(product.getId(), cart);
        //Store or update the shopping cart data into Redis cache
        redisTemplate.opsForHash().put(key, "items", cartItems);
        // Log success message
        LoggerFactory.getLogger(CartService.class).info("Product added to cart successfully!!!: {}", cart);
    }

    public void updateQuantity(Long id, int quantity){

        String key = getKey();

        //Get cart from the Redis cache
        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");

        if (cartItems == null){
            cartItems = new HashMap<>();
        }

        Cart product = cartItems.get(id);
        if (product == null){
            System.out.println("The product dose not exist to update quantity");
             return;
        }
        //plus more quantity
        product.setQuantity(quantity);

        //update into Map
        cartItems.put(id, product);
        //update into Redis cache
        redisTemplate.opsForHash().put(key, "items", cartItems);
    }

    public Cart getCartByProductId(Long id){
        String key = getKey();
        //Get cart from the Redis cache
        Map<Long, Cart> cartItems;
        cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");

        if (cartItems == null){
            cartItems = new HashMap<>();
        }

        Cart cart = cartItems.get(id);
        return cart;
    }


    public void removeProductFromCart(Long productId){
        String key = getKey();
        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");

        if (cartItems != null){
            cartItems.remove(productId);
            //Store or update the shopping cart data into Redis cache
            redisTemplate.opsForHash().put(key, "items", cartItems);
        }
    }


    //Get data of shopping cart
    public List<Cart> getCart(){
        String key = getKey();

        //Get cart form the Redis cache
        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");
        if (cartItems == null) {
            System.out.println("No items found in the cart");
            return null;
        }
        List<Cart> items = new ArrayList<>();
        for (Map.Entry<Long, Cart> entry : cartItems.entrySet()){
//            Long productId = entry.getKey();
            Cart cartEntry = entry.getValue();
            int quantity = cartEntry.getQuantity();
            Product product = cartEntry.getProduct();

            Cart cart = new Cart()
                    .setQuantity(quantity)
                    .setProduct(product);

            items.add(cart);
        }
        return items;
    }

    //Get total money of all products in the shopping cart
    public double getTotalAllItems(){
        String key = getKey();
        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");

        if (cartItems == null){
            return 0.0;
        }

        //Using this one like forEach
        return cartItems.values().stream().mapToDouble(Cart::getTotalPerProduct).sum();
    }

//    public double getTotalPerItem(String cartId){
//        String key = CART_PREFIX + cartId;
//        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");
//
//        if (cartItems == null){
//            return 0.0;
//        }
//
//        Cart cart = cartItems.get(key)
//
//        return cartItems.values().stream().mapToDouble(Cart::getTotalPerProduct);
//    }

    public void cleanCart(){
        String key = getKey();
        redisTemplate.delete(key);
    }


}






















