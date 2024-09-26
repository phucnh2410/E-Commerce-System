package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.ProductExtraImage;
import com.spring.ecommercesystem.services.FileUpload;
import com.spring.ecommercesystem.services.ProductExtraImageService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/extra_img")
public class ProductExtraImgRestController {
    private final ProductService productService;
    private final ProductExtraImageService productExtraImageService;
    private final UserService userService;

    private String DIRECTORY = "src/main/resources/static/productExtraImg/";

    @Autowired
    public ProductExtraImgRestController(ProductService productService, ProductExtraImageService productExtraImageService, UserService userService) {
        this.productService = productService;
        this.productExtraImageService = productExtraImageService;
        this.userService = userService;
    }

    @PostMapping("/save/{id}")
    public ResponseEntity<Map<String, Object>> saveExtraImg(@PathVariable Long id, @RequestPart(value = "list-product-img-files", required = false) List<MultipartFile> listFiles){
        Map<String, Object> response = new HashMap<>();
        Product product = this.productService.findById(id);

        if (product == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (listFiles == null || listFiles.isEmpty()){
            response.put("message", "No images uploaded.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        listFiles.forEach(file -> {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            ProductExtraImage productExtraImage = new ProductExtraImage()
                    .setProduct(product)
                    .setProductExtraImgSrc(fileName);

            this.productExtraImageService.saveAndUpdate(productExtraImage);

            String uploadDirectory = DIRECTORY + product.getId() + "/" + productExtraImage.getId();
            FileUpload.saveFile(uploadDirectory, fileName, file);
        });

        response.put("message", product.getName()+" was added successfully!!!");
        response.put("product", product);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }















}
