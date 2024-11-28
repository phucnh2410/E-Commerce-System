package com.spring.ecommercesystem.restController;


import com.spring.ecommercesystem.entities.Category;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CategoryService;
import com.spring.ecommercesystem.services.MailService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {
    private final CategoryService categoryService;
    private final MailService mailService;
    private final UserService userService;

    @Autowired
    public CategoryRestController(CategoryService categoryService, MailService mailService, UserService userService) {
        this.categoryService = categoryService;
        this.mailService = mailService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> allCategories(){
        List<Category> categories = this.categoryService.findAll();

        List<Category> categoriesApproved  = categories.stream()
                .filter(category -> category.getStatus() == Category.Status.Approved)//Get all categories with the status is approved
                .collect(Collectors.toList());

        return ResponseEntity.ok(categoriesApproved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Category category = this.categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<Map<String, Object>> createCategory(@PathVariable Long id, @RequestBody Category category){
        Map<String, Object> response = new HashMap<>();

        category.setStatus(Category.Status.Pending);
        User seller = this.userService.findById(id);
        try{
            this.categoryService.saveAndUpdate(category);

            this.mailService.sendRequestByMail("phuc97336@gmail.com", seller.getFullName(), category.getName());

            response.put("category", category);
            response.put("message", "Please wait for the admin to approve this category");

            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            System.err.println("Error while creating category: " + e.getMessage());
            response.put("message", "Sorry, something went wrong. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update_status/{id}/{status}")
    public ResponseEntity<Map<String, Object>> updateCategoryStatus(@PathVariable Long id, @PathVariable String status){
        Map<String, Object> response = new HashMap<>();
        Category category = this.categoryService.findById(id);
        if (category == null){
            System.out.println("Category not found");
        }

        try{
            if (status.equals("approved")){
                category.setStatus(Category.Status.Approved);
                response.put("message", "The "+category.getName()+" category has been approved");
            } else if (status.equals("reject")){
                category.setStatus(Category.Status.Reject);
                response.put("message", "The "+category.getName()+" category has been rejected, This one will be removed");
            }

            this.categoryService.saveAndUpdate(category);
            response.put("category", category);

            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            System.err.println("Error while creating category: " + e.getMessage());
            response.put("message", "Sorry, something went wrong. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
