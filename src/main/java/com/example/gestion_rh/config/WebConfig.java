package com.example.gestion_rh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads/cv}")
    private String uploadDir;

    @Value("${app.upload.url:/uploads/cv}")
    private String uploadUrl;

    @Value("${app.upload.images.dir:uploads/images}")
    private String imagesDir;

    @Value("${app.upload.images.url:/uploads/images}")
    private String imagesUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configuration pour les documents (CV)
        registry.addResourceHandler(uploadUrl + "/**")
                .addResourceLocations("file:" + uploadDir + "/");

        // Configuration pour les images
        registry.addResourceHandler(imagesUrl + "/**")
                .addResourceLocations("file:" + imagesDir + "/");
    }
}