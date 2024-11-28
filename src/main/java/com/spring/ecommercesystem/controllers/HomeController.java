package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.*;
import com.spring.ecommercesystem.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;

    private final OrderDetailService orderDetailService;

//    private String AVATARPATH = "src/main/resources/static/userAvatar/";//Local
    private String AVATARPATH = "/app/images/userAvatar/";//Docker


    @Autowired
    public HomeController(ProductService productService, UserService userService, CategoryService categoryService, OrderDetailService orderDetailService) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.orderDetailService = orderDetailService;
    }


    @GetMapping("/")
    public String home(HttpSession session, Model model){
        session.removeAttribute("userCarts");
        //Get all items in the OrderDetail table
        List<OrderDetail> orderDetails = this.orderDetailService.findAllItems();

        //Using the "Map<Long, Integer>" to get all products id and quantity for each product
        Map<Long, Integer> productSales = new HashMap<>();

        orderDetails.forEach(orderDetail -> {
            Long productId = orderDetail.getProduct().getId();
            int productQuantity = orderDetail.getProductQuantity();
            //If the products id are duplicated, increasing quantity of these product id

            //this code will replace if else statement above
            productSales.merge(productId, productQuantity, Integer::sum);
        });

        //transfer map to list map and Sorting from large to small quantity
        List<Map.Entry<Long, Integer>> sortedProducts = productSales.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(30)
                .collect(Collectors.toList());
        //show sorted products
        List<Product> productBestSellers = new ArrayList<>();

        sortedProducts.forEach(longIntegerEntry -> {
            Product product = this.productService.findById(longIntegerEntry.getKey());

            productBestSellers.add(product);
        });


        List<Product> newestProducts = this.productService.findNewestProducts();

        model.addAttribute("productBestSellers", productBestSellers);
        model.addAttribute("newestProducts", newestProducts);
        return "Home/homePage";
    }

    @GetMapping("/login")
    public String login(HttpSession session){
        session.invalidate();
        return "Home/loginAndRegister";
    }

    @GetMapping("/forbidden")
    public String forbidden(){
        return "ErrorPage/error403";
    }

    @GetMapping("/not_found")
    public String notFound(){
        return "ErrorPage/error404";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "Home/loginAndRegister";
    }

    @GetMapping("/profile")
    public String profile(Model model){
        User user = this.userService.getCurrentUser();

        Map<String, Object> profileData = this.userService.getProfileData(user);
        if (user.getRole().getName().equalsIgnoreCase("ROLE_CUSTOMER")) {
            model.addAttribute("numberOfOrder", profileData.get("numberOfOrder"));
            model.addAttribute("numberOfVoucher", profileData.get("numberOfVoucher"));
            model.addAttribute("user",  profileData.get("customer"));
            return "Home/profile";
        }

        model.addAttribute("totalRevenue", profileData.get("totalRevenue"));
        model.addAttribute("totalUnitsSold", profileData.get("totalUnitsSold"));
        model.addAttribute("totalProducts", profileData.get("totalProducts"));
        model.addAttribute("totalProductsSold", profileData.get("totalProductsSold"));

        model.addAttribute("user", profileData.get("seller"));
        return "Home/profile";
    }

    @GetMapping("/change-password-form")
    public String showFormChangePassword(){

        return "Home/changePassword";
    }

//    @PostMapping("/change-password-save")
//    public String saveChangePassword(@RequestParam("newPassword") String newPassword, HttpSession session, Model model){
//        String email = (String) session.getAttribute("emailUserChangePassword");
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        // Kiểm tra nếu email không có trong session
//        if (email.equals(null) || email.isEmpty()) {
//            model.addAttribute("message", "No email found in session. Please try again.");
//            return "Home/changePassword"; // Trả về trang hiện tại thay vì chuyển hướng
//        }
//
//        // Kiểm tra nếu mật khẩu mới rỗng
//        if (newPassword.equals(null) || newPassword.isEmpty()) {
//            model.addAttribute("message", "Please enter a new password.");
//            return "Home/changePassword"; // Trả về trang hiện tại thay vì chuyển hướng
//        }
//
//        System.out.println("email: "+email);
//        System.out.println("new pass: "+newPassword);
//
//        User user = this.userService.findByEmail(email);
//        user.setPassword(encoder.encode(newPassword));
//
//        this.userService.saveAndUpdate(user);
//        session.removeAttribute("emailUserChangePassword");
//        return "redirect:/login";
//    }

    @PostMapping("/save/profile")
    public String updateProfile(@ModelAttribute("user") User user, @RequestParam("avatar-file")MultipartFile file) throws IOException {
        User userDB = this.userService.findById(user.getId());

        if (userDB == null){
            System.out.println("UserDB is null!!!");
        }

        if (!file.isEmpty()){
            //Delete the old avatar
            String oldAvatarName = userDB.getAvatar();
            if (oldAvatarName != null && !oldAvatarName.isEmpty()){
                String oldAvatarPath = AVATARPATH + user.getId() + "/" + oldAvatarName;
                File oldFile = new File(oldAvatarPath);
                if (oldFile.exists()){
                    oldFile.delete();
                }
            }

            //Get and set the new avatar
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            user.setAvatar(fileName);
            user.setPassword(userDB.getPassword());
            user.setRole(userDB.getRole());
            user.setExpenditure(userDB.getExpenditure());
            user.setCreatedDate(userDB.getCreatedDate());

            user.setCustomerType(userDB.getCustomerType());

            if (user.getDateOfBirth() == null){
                user.setDateOfBirth(userDB.getCreatedDate());
            }else {
                user.setDateOfBirth(userDB.getDateOfBirth());
            }

            this.userService.saveAndUpdate(user);

            //Save new image into server folder
            String uploadDirectory = AVATARPATH + user.getId();
            FileUpload.saveFile(uploadDirectory, fileName, file);
        }else {
            System.out.println("Multipart file is null!!!");
            if (userDB.getAvatar().isEmpty()) {
                user.setAvatar(null);
            }
            user.setAvatar(userDB.getAvatar());
            user.setPassword(userDB.getPassword());
            user.setRole(userDB.getRole());
            user.setExpenditure(userDB.getExpenditure());
            user.setCreatedDate(userDB.getCreatedDate());
            user.setCustomerType(userDB.getCustomerType());

            if (user.getDateOfBirth() == null){
                user.setDateOfBirth(userDB.getCreatedDate());
            }else {
                user.setDateOfBirth(userDB.getDateOfBirth());
            }
        }

        this.userService.saveAndUpdate(user);
        return "redirect:/profile";
    }

    @GetMapping("/shop")
    public String showShopping(Model model, @RequestParam("id") Long id){
        User user = this.userService.findById(id);
        List<Product> products = user.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("totalProduct", products.size());
        model.addAttribute("user", user);

        return "Shop/shopManagement";
    }

    @GetMapping("/product_detail")
    public String showProductDetail(Model model, @RequestParam("id") Long id, HttpSession session){
        session.removeAttribute("userCarts");
        Product product = this.productService.findById(id);

        //Get all items in orderDetail from the product
        List<OrderDetail> orderDetails = product.getOrderDetails();
        int numberOfProductSale = 0;

        for (OrderDetail orderDetail : orderDetails){
            numberOfProductSale += orderDetail.getProductQuantity();
        }

        List<Feedback> feedbacks = product.getFeedbacks();

        OptionalDouble getRatingAverage = feedbacks.stream().mapToDouble(Feedback::getFeedbackRating).average();

        double ratingAverage = getRatingAverage.orElse(0.0); // Trả về 0.0 nếu không có giá trị trung bình

        model.addAttribute("numberOfProductSale", numberOfProductSale);
        model.addAttribute("numberOfRating", feedbacks.size());
        model.addAttribute("ratingAverage", ratingAverage);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("product", product);
        return "Product/productDetail";
    }

}














