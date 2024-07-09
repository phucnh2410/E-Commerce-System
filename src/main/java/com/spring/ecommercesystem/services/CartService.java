package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Cart;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
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
    private static final String CART_PREFIX = "cart:";

    @Autowired
    public CartService(RedisTemplate<String, Object> redisTemplate, UserService userService) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
    }

//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userAuth = (UserDetails) authentication.getPrincipal();
//        if (userAuth == null){
//            System.out.println("User does not authenticate!!!");
//        }
//        Collection<? extends GrantedAuthority> authorities = userAuth.getAuthorities();
//
//        boolean isCustomer = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_CUSTOMER"));
//
//        if (isCustomer) {
//            User user = this.userService.getCurrentUser();
//            //get or generate cartId by session
//            String cartId = this.cartService.getCartId(user.getId());
//            if (cartId == null) {
//                System.out.println("Cart ID is null!!!");
//            }

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

    //Get the number of already products in the cart of the user
    public Long getTotalItemsInCart(){
        String cartId = getCartId();
        if (cartId == null){
            System.out.println("Cart ID is null in the getTotalItemsInCart() method");
            return 0L;
        }

        //Using the cart id to get a shopping cart
        String key = CART_PREFIX + cartId;

        //Get cart from the Redis cache
        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");
        if (cartItems == null){
            return 0L;
        }

        //Get the total of already products in the cart
        return (long) cartItems.size();
    }

    public void addProductToCart(Product product, int quantity){
        String cartId = getCartId();
        if (cartId == null){
            System.out.println("Cart ID is null in the addProductToCart() method");
            return;
        }
        String key = CART_PREFIX + cartId;
        //Get cart from the Redis cache
        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");

        if (cartItems == null){
            cartItems = new HashMap<>();
        }

        //Check if the product exists in the shopping cart?
        Cart cart = cartItems.get(product.getId());
        if (cart == null){
            cart = new Cart();
            cart.setProduct(product);
            cart.setQuantity(quantity);
        }else {
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        cartItems.put(product.getId(), cart);
        //Store or update the shopping cart data into Redis cache
        redisTemplate.opsForHash().put(key, "items", cartItems);
        System.out.println("Add product to cart successfully!!!");
    }

    public void removeProductFromCart(Long productId){
        String cartId = getCartId();
        if (cartId == null){
            System.out.println("Cart ID is null in the removeProductFromCart() method");
            return;
        }
        String key = CART_PREFIX + cartId;
        Map<Long, Cart> cartItems = (Map<Long, Cart>) redisTemplate.opsForHash().get(key, "items");

        if (cartItems != null){
            cartItems.remove(productId);
            //Store or update the shopping cart data into Redis cache
            redisTemplate.opsForHash().put(key, "items", cartItems);
        }
    }


    //Get data of shopping cart
    public List<Cart> getCart(){
        String cartId = getCartId();
        if (cartId == null){
            System.out.println("Cart ID is null in the getCart() method");
            return null;
        }
        String key = CART_PREFIX + cartId;

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
        String cartId = getCartId();
        if (cartId == null){
            System.out.println("Cart ID is null in the getTotalAllItems() method");
            return 0.0;
        }
        String key = CART_PREFIX + cartId;
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
        String cartId = getCartId();
        if (cartId == null){
            System.out.println("Cart ID is null in the cleanCart() method");
            return;
        }
        String key = CART_PREFIX + cartId;
        redisTemplate.delete(key);
    }


}






















