package com.spring.ecommercesystem.repositories;

import com.spring.ecommercesystem.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p ORDER BY p.createAt DESC")
    List<Product> findNewestProducts();

    List<Product> findProductByNameContainingIgnoreCase(String name);


}
