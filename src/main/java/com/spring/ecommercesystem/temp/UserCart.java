package com.spring.ecommercesystem.temp;

import com.spring.ecommercesystem.entities.User;

import java.util.List;

public class UserCart {
    private User user;

    private List<CartTemp> cartTemps;

    public UserCart() {
    }

    public UserCart(User user, List<CartTemp> cartTemps) {
        this.user = user;
        this.cartTemps = cartTemps;
    }

    public User getUser() {
        return user;
    }

    public UserCart setUser(User user) {
        this.user = user;
        return this;
    }

    public List<CartTemp> getCartTemps() {
        return cartTemps;
    }

    public UserCart setCartTemps(List<CartTemp> cartTemps) {
        this.cartTemps = cartTemps;
        return this;
    }
}
