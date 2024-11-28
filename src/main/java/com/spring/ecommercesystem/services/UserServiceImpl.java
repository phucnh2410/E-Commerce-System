package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.*;
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

import java.util.*;
import java.util.stream.Collectors;

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
        if (user == null) {
            throw new UsernameNotFoundException("'UserServiceImpl.class said': User not found!!!");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getRoleToAuthorities(user.getRole()));
    }


    private Collection<? extends GrantedAuthority> getRoleToAuthorities(Role role) {
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
    public boolean isAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu không có authentication hoặc người dùng chưa được xác thực
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("User is not authenticated!");
            return false;
        }

        // Lấy thông tin principal từ authentication
        Object principal = authentication.getPrincipal();

        // Kiểm tra nếu principal là String hoặc "anonymousUser" (người dùng chưa đăng nhập)
        if (principal instanceof String && principal.equals("anonymousUser")) {
            System.out.println("User is not authenticated (anonymous)!");
            return false;
        }

        // Kiểm tra nếu principal là DefaultOAuth2User (OAuth2-based login)
        if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            String email = oAuth2User.getAttribute("email");
            if (email != null && !email.isEmpty()) {
                System.out.println("User is authenticated via OAuth2: " + email);
                return true;
            }
            System.out.println("OAuth2 user does not have a valid email attribute.");
            return false;
        }

        // Kiểm tra nếu principal là UserDetails (username-password login)
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            if (username != null && !username.isEmpty()) {
                System.out.println("User is authenticated via username-password: " + username);
                return true;
            }
            System.out.println("UserDetails object does not have a valid username.");
            return false;
        }

        // Nếu principal không thuộc kiểu nào được hỗ trợ
        System.out.println("Unsupported authentication principal type: " + principal.getClass().getName());
        return false;
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

        // Kiểm tra nếu principal là DefaultOAuth2User (OAuth2 login)
        if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            email = oAuth2User.getAttribute("email");
        }

        // Kiểm tra nếu principal là UserDetails (username-password login)
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            email = userDetails.getUsername();
        }

        if (!(principal instanceof DefaultOAuth2User) && !(principal instanceof UserDetails)) {
//            throw new IllegalStateException("Unsssupported authentication principal type");
        }

        if (email == null) {
//            throw new IllegalStateException("Email not found in this Authentication");
        }

        user = this.userRepository.findByEmail(email);
        if (user == null) {
//            throw new IllegalStateException("User not found in the database");
        }

        return user;
    }

    @Override
    public boolean isCustomer() {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != null && currentUser.getRole().getName().equalsIgnoreCase("ROLE_CUSTOMER")){
            return true;
        }

        return false;
    }

    @Override
    public boolean isSeller() {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != null && currentUser.getRole().getName().equalsIgnoreCase("ROLE_SELLER")){
            return true;
        }

        return false;
    }

    @Override
    public boolean isAdmin() {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != null && currentUser.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")){
            return true;
        }

        return false;
    }

    @Override
    public Map<String, Object> getProfileData(User user) {
        Map<String, Object> profileData = new HashMap<>();

        if (user.getRole().getName().equalsIgnoreCase("ROLE_CUSTOMER")) {
            return getCustomerProfileData(user, profileData);
        }

        if (user.getRole().getName().equalsIgnoreCase("ROLE_SELLER")) {
            return getSellerProfileData(user, profileData);
        }

        return profileData;
    }

    @Override
    public Map<String, Object> getCustomerProfileData(User user, Map<String, Object> profileData) {
        List<VoucherDetail> vouchersOwnedUnused = user.getVoucherDetails().stream().filter(voucherDetail -> voucherDetail.getStatus() == VoucherDetail.Status.Unused).collect(Collectors.toList());
        profileData.put("numberOfOrder", user.getOrders().size());
        profileData.put("numberOfVoucher", vouchersOwnedUnused.size());
        profileData.put("customer", user);
        return profileData;
    }

    @Override
    public Map<String, Object> getSellerProfileData(User user, Map<String, Object> profileData) {
        List<Product> products = user.getProducts();

        List<OrderDetail> totalProductsSold = new ArrayList<>();
//        Double totalRevenue = products.stream().mapToDouble(product -> (product.getPrice() * (product.getOrderDetails().stream().mapToInt(orderDetail -> orderDetail.getProductQuantity()).sum()) )).sum();

        Double totalRevenue = 0.0;
        int totalUnitsSold = 0;
        for (Product product : products) {
            Double price = product.getPrice();
            int quantity = 0;

            for (OrderDetail od : product.getOrderDetails()) {
                if (od.getOrder().getStatus().name().equalsIgnoreCase("Received")) {
                    quantity += od.getProductQuantity();
                    totalUnitsSold += od.getProductQuantity();

                    totalProductsSold.add(od);
                } else {
                    continue;
                }
            }

            totalRevenue += price * quantity;
        }

        profileData.put("totalRevenue", totalRevenue);
        profileData.put("totalUnitsSold", totalUnitsSold);
        profileData.put("totalProducts", products.size());
        profileData.put("totalProductsSold", totalProductsSold.size());
        profileData.put("seller", user);
        return profileData;
    }
















}











































