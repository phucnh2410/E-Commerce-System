package com.spring.ecommercesystem.services;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ShowImageOnView implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/userAvatar/**")
                .addResourceLocations("classpath:/static/userAvatar/")
                .setCacheControl(CacheControl.noCache().mustRevalidate());
    }

//    private void exposeUserDirectory(ResourceHandlerRegistry registry) {
//        String dirName = "/userAvatar";
//
//        Path uploadDir = Paths.get(dirName);
//        String uploadPath = uploadDir.toFile().getAbsolutePath();
//
////        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
//
//        registry.addResourceHandler("/" + dirName + "/**")
//                .addResourceLocations("file:/"+ uploadPath + "/")
//                .setCacheControl(CacheControl.noCache().mustRevalidate()); //Disable caching
//    }
}
