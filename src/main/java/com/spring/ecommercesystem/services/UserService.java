package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService{

    public List<User> findAll();
    public User findById(Long id);
    public User findByEmail(String email);
    public void saveAndUpdate(User user);
    public void deleteById(Long id);

    public User getCurrentUser();

    public boolean isAuth();
    public boolean isCustomer();
    public boolean isSeller();
    public boolean isAdmin();

    Map<String, Object> getProfileData(User user);

    Map<String, Object> getCustomerProfileData(User user, Map<String, Object> profileData);
    Map<String, Object> getSellerProfileData(User user, Map<String, Object> profileData);
}
