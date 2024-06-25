package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Category;
import com.spring.ecommercesystem.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveAndUpdate(Category category) {
        this.categoryRepository.saveAndFlush(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.categoryRepository.deleteById(id);
    }
}
