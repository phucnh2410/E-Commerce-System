package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Product;
import com.spring.ecommercesystem.entities.ProductExtraImage;
import com.spring.ecommercesystem.services.ProductExtraImageService;
import com.spring.ecommercesystem.services.ProductService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/api/download")
public class DownloadFile {
    private final UserService userService;
    private final ProductService productService;

    private final ProductExtraImageService productExtraImageService;

    @Autowired
    public DownloadFile(UserService userService, ProductService productService, ProductExtraImageService productExtraImageService) {
        this.userService = userService;
        this.productService = productService;
        this.productExtraImageService = productExtraImageService;
    }

    @GetMapping("/{object}/{id}/{fileName}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("object") String object, @PathVariable("id") Long Id, @PathVariable("fileName") String fileName) throws IOException {

        Path filePath = Paths.get("src/main/resources/static/"+object).resolve(String.valueOf(Id)).resolve(fileName);
        // Chuyển đường dẫn sang dạng tuyệt đối.
        Path absolutePath = filePath.toAbsolutePath().normalize();

//        System.out.println(absolutePath);

        if (!Files.exists(absolutePath)){
            throw new FileNotFoundException(fileName + " was not found on the server");
        }
        Resource resource = new UrlResource(absolutePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", fileName);
        httpHeaders.add(CONTENT_DISPOSITION, "attament;File-Name=" + resource.getFilename());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(absolutePath)))
                .headers(httpHeaders).body(resource);
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<String>> downloadProductExtraImg(@PathVariable Long id) {

        Product product = this.productService.findById(id);
        if (product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<ProductExtraImage> productImages = product.getProductExtraImages();
        List<String> imageUrls = new ArrayList<>();

        productImages.forEach(proImg -> {
            String imageUrl = "/productExtraImg/" +product.getId()+ "/" + proImg.getId() + "/" + proImg.getProductExtraImgSrc();
            imageUrls.add(imageUrl);
        });

        return ResponseEntity.ok(imageUrls);
    }





}
