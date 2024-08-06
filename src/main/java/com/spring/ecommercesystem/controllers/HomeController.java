package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;

    private final OrderDetailService orderDetailService;


    @Autowired
    public HomeController(ProductService productService, UserService userService, CategoryService categoryService, OrderDetailService orderDetailService) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.orderDetailService = orderDetailService;
    }


    @GetMapping("/")
    public String home(Model model){
        //Get all items in the OrderDetail table
        List<OrderDetail> orderDetails = this.orderDetailService.findAllItems();

        //Using the "Map<Long, Integer>" to get all products id and quantity for each product
        Map<Long, Integer> productSales = new HashMap<>();

        orderDetails.forEach(orderDetail -> {
            Long productId = orderDetail.getProduct().getId();
            int productQuantity = orderDetail.getProductQuantity();
            //If the products id are duplicated, increasing quantity of these product id
//            if (productSales.containsKey(productId)){
//                productSales.put(productId, productSales.get(productId) + productQuantity);// 'productSales.get(productId) + productQuantity" get value by key and then plus more into this value
//                return;
//            }
//            productSales.put(productId, productQuantity);

            //this code will replace if else statement above
            productSales.merge(productId, productQuantity, Integer::sum);
        });

        //transfer map to list map and Sorting from large to small quantity
        List<Map.Entry<Long, Integer>> sortedProducts = productSales.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(30)
                .collect(Collectors.toList());
        //show sorted products
//        sortedProducts.forEach(longIntegerEntry -> System.out.println("Product id: "+longIntegerEntry.getKey() +" -> "+ "Quantity: "+longIntegerEntry.getValue()));

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














