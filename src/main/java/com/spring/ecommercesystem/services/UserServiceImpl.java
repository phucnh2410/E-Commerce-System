package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Role;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //Authenticate
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("'UserServiceImpl.class said': User not found!!!");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getRoleToAuthorities(user.getRole()));
    }


    private Collection<? extends GrantedAuthority> getRoleToAuthorities(Role role){
        String roleName = role.getName();
        if (roleName == null || roleName.isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        return Collections.singleton(authority);
    }


    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findById(id).get();
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void saveAndUpdate(User user) {
        this.userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
//            throw new IllegalStateException("No authenticated user found");
        }
        Object principal = authentication.getPrincipal();

        String email = null;
        User user;
        if (principal instanceof DefaultOAuth2User){
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            email = oAuth2User.getAttribute("email");
        }

        if (principal instanceof UserDetails){
            UserDetails userDetails = (UserDetails) principal;
            email = userDetails.getUsername();
        }

        if (!(principal instanceof DefaultOAuth2User) && !(principal instanceof UserDetails)){
//            throw new IllegalStateException("Unsssupported authentication principal type");
        }

        if (email == null){
//            throw new IllegalStateException("Email not found in this Authentication");
        }

        user = this.userRepository.findByEmail(email);
        if (user == null){
//            throw new IllegalStateException("User not found in the database");
        }

        return user;
    }


}
