package com.zipnivasa.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Exposes the physical "uploads" folder as /uploads/** URLs,
 * just like app.use("/uploads", express.static(...)).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-base-dir:uploads}")
    private String uploadBaseDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String root = System.getProperty("user.dir");
        Path uploadDir = Paths.get(root, uploadBaseDir).toAbsolutePath().normalize();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadDir.toUri().toString());
    }

}
