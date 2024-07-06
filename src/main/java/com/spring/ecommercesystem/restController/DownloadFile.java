package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/api/download")
public class DownloadFile {
    private final UserService userService;

    @Autowired
    public DownloadFile(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{object}/{id}/{fileName}")
    public ResponseEntity<Resource> downloadAvatarFiles(Model model, @PathVariable("object") String object, @PathVariable("id") Long Id, @PathVariable("fileName") String fileName) throws IOException {

        Path filePath = Paths.get("src/main/resources/static/"+object).resolve(String.valueOf(Id)).resolve(fileName);
        // Chuyển đường dẫn sang dạng tuyệt đối.
        Path absolutePath = filePath.toAbsolutePath().normalize();

        System.out.println(absolutePath);

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
}
