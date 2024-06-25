package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll();
    public Category findById(Long id);
    public void saveAndUpdate(Category category);
    public void deleteById(Long id);
}
