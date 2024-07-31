package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.CategoryService;
import com.spring.ecommercesystem.services.FileUpload;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/product")
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



    @GetMapping("{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id){
        Product product = this.productService.findById(id);
        if (product == null){
            System.out.println("Product does not exist!!!");
            ResponseEntity.badRequest();
        }
//        System.out.println("Product Object to show form edit: "+product);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addProduct(@RequestPart(value = "product") Product product, @RequestPart(value = "product-file", required = false) MultipartFile file) throws IOException{
        Map<String, Object> response = new HashMap<>();

        if (product == null){
            System.out.println("Product received is null!!!");
        }

        System.out.println("Received Product: " + product);
        System.out.println("Received Product: " + product.getId());

        //Update
        if (product.getId() != null){
            Product oldProduct = this.productService.findById(product.getId());

            if (file != null || !file.isEmpty()){
                //Delete old image
                String oldImageName = oldProduct.getProductImg();
                if (oldImageName != null || !oldImageName.isEmpty()){
                    String oldImagePath = "src/main/resources/static/productImg/" +oldProduct.getId()+ "/" +oldImageName;
                    File oldFile = new File(oldImagePath);
                    if (oldFile.exists()){
                        oldFile.delete();
                    }
                }

                //Add new image
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                product.setProductImg(fileName);
                product.setUser(this.userService.getCurrentUser());
                String uploadDirectory = "src/main/resources/static/productImg/" + product.getId();
                FileUpload.saveFile(uploadDirectory, fileName, file);
                this.productService.saveAndUpdate(product);

                response.put("message", product.getName()+" was updated successfully!!!");
                response.put("product", product);
            }else {
                // Keep the existing image if no new file is uploaded
                product.setProductImg(oldProduct.getProductImg());
            }
        }

        //Add new
        if (product.getId() == null){

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            product.setProductImg(fileName);
            product.setUser(this.userService.getCurrentUser());
            this.productService.saveAndUpdate(product);
            String uploadDirectory = "src/main/resources/static/productImg/" + product.getId();
            FileUpload.saveFile(uploadDirectory, fileName, file);

            response.put("message", product.getName()+" was added successfully!!!");
            response.put("product", product);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        try{
            this.productService.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

}
