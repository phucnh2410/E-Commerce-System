package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.ProductExtraImage;

import java.util.List;

public interface ProductExtraImageService {
    List<ProductExtraImage> findAll();
    ProductExtraImage findById(Long id);

    void saveAndUpdate(ProductExtraImage productExtraImage);

    void deleteById(Long id);
}
