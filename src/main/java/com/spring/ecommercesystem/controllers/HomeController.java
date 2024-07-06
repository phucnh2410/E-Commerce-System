package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CategoryService;
import com.spring.ecommercesystem.services.FileUpload;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;


    @Autowired
    public HomeController(ProductService productService, UserService userService, CategoryService categoryService) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @GetMapping("/")
    public String home(Model model){
        List<Product> products = this.productService.findAll();
        model.addAttribute("products", products);
        return "Home/homePage";
    }

    @GetMapping("/login")
    public String login(){
        return "Home/loginAndRegister";
    }

    @GetMapping("/profile")
    public String profile(Model model){
//        User user = this.userService.getCurrentUser();
        model.addAttribute("user", this.userService.getCurrentUser());
        return "Home/profile";
    }

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
                String oldAvatarPath = "src/main/resources/static/userAvatar/" + user.getId() + "/" + oldAvatarName;
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
            this.userService.saveAndUpdate(user);

            //Save new image into server folder
            String uploadDirectory = "src/main/resources/static/userAvatar/" + user.getId();
            FileUpload.saveFile(uploadDirectory, fileName, file);
        }else {
            System.out.println("Multipart file is null!!!");
            if (userDB.getAvatar().isEmpty()) {
                user.setAvatar(null);
            }
            user.setAvatar(userDB.getAvatar());
        }

        this.userService.saveAndUpdate(user);
        return "redirect:/profile";
    }

    @GetMapping("/shop")
    public String showShopping(Model model, @RequestParam("id") Long id){
        //get authorize
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userAuth = (UserDetails) authentication.getPrincipal();
//        if (userAuth == null){
//            System.out.println("User does not authenticate!!!");
//        }
//        Collection<? extends GrantedAuthority> authorities = userAuth.getAuthorities();
//
//        boolean isSeller = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_SELLER"));
//
//
//        if (isSeller){
//            User user = this.userService.findById(id);
//            List<Product> products = user.getProducts();
//            model.addAttribute("products", products);
//            model.addAttribute("user", user);
//        }

        User user = this.userService.findById(id);
        List<Product> products = user.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("user", user);

        return "Shop/shopManagement";
    }

    @GetMapping("/product_detail")
    public String showProductDetail(Model model, @RequestParam("id") Long id){
        Product product = this.productService.findById(id);
        model.addAttribute("product", product);
        return "Product/productDetail";
    }

}














