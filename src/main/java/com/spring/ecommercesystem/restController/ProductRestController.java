package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Order;
import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/product")
public class ProductRestController {
    private final CategoryService categoryService;
    private final ProductService productService;

    private final OrderDetailService orderDetailService;

    private final OrderService orderService;

    private final UserService userService;

//    private String DIRECTORY = "src/main/resources/static/productImg/";//local
    private String DIRECTORY = "/app/images/productImg/";//Docker

    @Autowired
    public ProductRestController(CategoryService categoryService, ProductService productService, OrderDetailService orderDetailService, OrderService orderService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> allProducts(){
        User currentSeller = this.userService.getCurrentUser();
        List<Product> products = currentSeller.getProducts();
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

    @PutMapping("/update_stock/{id}/{stock}")
    public ResponseEntity<Map<String, Object>> updateStock(@PathVariable("id") Long id, @PathVariable("stock") int stock){
        Map<String, Object> response = new HashMap<>();

        if (id == null){
            response.put("error", "Product ID can be not found");
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);//415
        }

        if (stock == 0){
            response.put("error", "Stock = 0");
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);//415
        }

        Product product = this.productService.findById(id);
        if (product == null){
            response.put("error", "Product does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);//404
        }

        product.setStock(product.getStock() + stock);
        this.productService.saveAndUpdate(product);

        response.put("currentStock", product.getStock());
        return ResponseEntity.status(HttpStatus.OK).body(response);//200
    }

    @PutMapping("/update_product_status/{orderId}/{productId}")
    public ResponseEntity<Map<String, Object>> updateProductStatus(@PathVariable("orderId") Long orderId, @PathVariable("productId") Long productId){
        Map<String, Object> response = new HashMap<>();

        if (productId == null){
            response.put("error", "Product ID can be not found");
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);//415
        }

        if (orderId == null){
            response.put("error", "Order ID can be not found");
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);//415
        }

        OrderDetail orderDetail = this.orderDetailService.updateStatusForEachProduct(orderId, productId);
        System.out.println("Product Status in Order Detail: "+orderDetail.getProductStatus().name());

        Order order = this.orderService.findById(orderId);
        List<OrderDetail> orderDetails = order.getOrderDetails();

        int numberOfItemInOrder = orderDetails.size();
        int numberOfItemConfirmed = (int) orderDetails.stream().filter(od -> od.getProductStatus().name().equalsIgnoreCase(OrderDetail.ProductStatus.Confirmed.name())).count();

        if (numberOfItemInOrder == numberOfItemConfirmed){
            order.setStatus(Order.Status.Confirmed);
            order.setConfirmedDate(new Date(System.currentTimeMillis()));
            this.orderService.saveAndUpdate(order);
            response.put("orderStatus", order.getStatus());
        }

        response.put("success", "This product has been confirmed in this order.");
        return ResponseEntity.status(HttpStatus.OK).body(response);//200
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addProduct(@RequestPart(value = "product") Product product, @RequestPart(value = "product-file", required = false) MultipartFile file){
        Map<String, Object> response = new HashMap<>();

        if (product == null){
            System.out.println("Product received is null!!!");
        }

        //Update
        if (product.getId() != null){
            Product oldProduct = this.productService.findById(product.getId());

            if (file != null || !file.isEmpty()){
                //Delete old image
                String oldImageName = oldProduct.getProductImg();
                if (oldImageName != null || !oldImageName.isEmpty()){
                    String oldImagePath = DIRECTORY + oldProduct.getId()+ "/" +oldImageName; // String oldImagePath = "src/main/resources/static/productImg/" +oldProduct.getId()+ "/" +oldImageName;
                    File oldFile = new File(oldImagePath);
                    if (oldFile.exists()){
                        oldFile.delete();
                    }
                }

                //Add new image
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                product.setProductImg(fileName);
                product.setUser(this.userService.getCurrentUser());
                product.setCreateAt(new Date(System.currentTimeMillis()));
                String uploadDirectory = DIRECTORY + product.getId(); // String uploadDirectory = "src/main/resources/static/productImg/" + product.getId();
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
            product.setCreateAt(new Date(System.currentTimeMillis()));

            this.productService.saveAndUpdate(product);
            String uploadDirectory = DIRECTORY + product.getId();
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
