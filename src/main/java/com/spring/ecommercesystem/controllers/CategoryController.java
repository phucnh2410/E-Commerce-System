package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.entities.Category;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public String showProductByCategory(@PathVariable("id") Long id, Model model){
        Category category = this.categoryService.findById(id);
        List<Product> products = category.getProducts();
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        return "PageForEachCategory/categoryPage";
    }
}
