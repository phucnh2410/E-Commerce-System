package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).get();
    }

    @Override
    public List<Product> findByName(String name) {
        return this.productRepository.findProductByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> findNewestProducts() {
        return this.productRepository.findNewestProducts();
    }

    @Override
    @Transactional
    public void saveAndUpdate(Product product) {
        this.productRepository.saveAndFlush(product);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }
}
