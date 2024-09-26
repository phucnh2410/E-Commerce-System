package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.ProductExtraImage;
import com.spring.ecommercesystem.repositories.ProductExtraImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductExtraImageServiceImpl implements ProductExtraImageService {
    private final ProductExtraImageRepository productExtraImageRepository;

    @Autowired
    public ProductExtraImageServiceImpl(ProductExtraImageRepository productExtraImageRepository) {
        this.productExtraImageRepository = productExtraImageRepository;
    }


    @Override
    public List<ProductExtraImage> findAll() {
        return this.productExtraImageRepository.findAll();
    }

    @Override
    public ProductExtraImage findById(Long id) {
        return this.productExtraImageRepository.findById(id).get();
    }

    @Override
    public void saveAndUpdate(ProductExtraImage productExtraImage) {
        this.productExtraImageRepository.saveAndFlush(productExtraImage);
    }

    @Override
    public void deleteById(Long id) {
        this.productExtraImageRepository.deleteById(id);
    }
}
