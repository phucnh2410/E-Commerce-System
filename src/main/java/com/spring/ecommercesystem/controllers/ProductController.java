package com.spring.ecommercesystem.controllers;

import com.spring.ecommercesystem.dto.ProductDTO;
import com.spring.ecommercesystem.entities.OrderDetail;
import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping()
    public String allProducts(Model model){
        User currentUser = this.userService.getCurrentUser();
        List<Product> products = currentUser.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("user", currentUser);

        return "Product/productManagement";
    }

    @GetMapping("/management-fragment")
    public String productManagementFragment(Model model){
        User currentUser = this.userService.getCurrentUser();
        List<Product> products = currentUser.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("user", currentUser);

        return "Product/productManagement :: productManagementFrag";
    }

    @GetMapping("/sold-fragment")
    public String productSoldFragment(Model model){
        User currentUser = this.userService.getCurrentUser();
        List<Product> products = currentUser.getProducts();

        List<ProductDTO> productDTOS = new ArrayList<>();

        for (Product product : products){
            Long productId = product.getId();
            String productName = product.getName();
            String productImg = product.getProductImg();
            Double productPrice = product.getPrice();

            Integer totalQtySold = 0;
            Long orderId = null;
//            Double totalRevenueEachProduct = 0.0;
            String customerName = "";
            Date orderedDate = null;
            String status = "";
            for (OrderDetail orderDetail : product.getOrderDetails()){
                orderId = orderDetail.getOrder().getId();
                totalQtySold = orderDetail.getProductQuantity();
                orderedDate = orderDetail.getOrder().getOrderedDate();
                status = orderDetail.getOrder().getStatus().name();
                customerName = orderDetail.getOrder().getUser().getFullName();

                if (orderId != null) {
                    ProductDTO productDTO = new ProductDTO()
                            .setId(productId)
                            .setName(productName)
                            .setImg(productImg)
                            .setOrderId(orderId)
                            .setOrderedDate(orderedDate)
                            .setCustomerName(customerName)
                            .setPrice(productPrice)
                            .setQtySold(totalQtySold)
                            .setOrderStatus(status);

                    productDTOS.add(productDTO);
                }else {
                    continue;
                }
            }
        }

        model.addAttribute("productDTOs", productDTOS);
        model.addAttribute("totalProductSold", productDTOS.size());
        model.addAttribute("user", currentUser);

        return "Product/productManagement :: productSoldFrag";
    }

    @GetMapping("/search")
    public String productSearch(@RequestParam("name") String name, Model model){
        List<Product> products = this.productService.findByName(name);
        model.addAttribute("products", products);
        return "Product/productSearching";
    }
}




















