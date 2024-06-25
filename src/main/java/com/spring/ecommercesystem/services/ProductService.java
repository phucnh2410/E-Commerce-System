package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Product;

import java.util.List;

public interface ProductService {
    public List<Product> findAll();
    public Product findById(Long id);
    public void saveAndUpdate(Product product);
    public void deleteById(Long id);
}
