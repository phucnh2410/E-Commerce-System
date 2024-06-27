package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{

    public List<User> findAll();
    public User findById(Long id);
    public User findByEmail(String email);
    public void saveAndUpdate(User user);
    public void deleteById(Long id);

    public User getCurrentUser();
}
