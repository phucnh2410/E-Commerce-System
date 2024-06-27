package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CategoryService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {
    private final CategoryService categoryService;
    private final ProductService productService;

    private final UserService userService;

    @Autowired
    public ProductRestController(CategoryService categoryService, ProductService productService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> allProducts(){
        User currentUser = this.userService.getCurrentUser();
        List<Product> products = currentUser.getProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Product product){
        Map<String, Object> response = new HashMap<>();
        if (product == null){
            response.put("message", "Product is null");
        }

        //Update case
//        Product oldProduct = this.productService.findById(product.getId());
//        if (oldProduct ==null){
//            response.put("message", "Product not found!");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//
//        if (!file.isEmpty()) {
//            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//            product.setProductImg(fileName);
//            this.productService.saveAndUpdate(product);
//
//            String uploadDirectory = "src/main/resources/static/productImg/" + product.getId();
//            FileUpload.saveFile(uploadDirectory, fileName, file);
//        }else {
//            response.put("message", "Product image does not exist!!!");
//            if (oldProduct.getProductImg().isEmpty() || oldProduct.getProductImg() == null) {
//                product.setProductImg(null);
//            }
//            product.setProductImg(oldProduct.getProductImg());
//        }
        product.setUser(this.userService.getCurrentUser());
//
//        this.productService.saveAndUpdate(product);

        System.out.println("Product Object: " + product);
        System.out.println("Product Object: " + product.getUser());
        System.out.println("Product Object: " + product.getCategory());

        response.put("message", "Product added successfully!!!");
        response.put("product", product);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
