package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CategoryService;
import com.spring.ecommercesystem.services.FileUpload;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

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


    @GetMapping("/home")
    public String home(){
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

}














