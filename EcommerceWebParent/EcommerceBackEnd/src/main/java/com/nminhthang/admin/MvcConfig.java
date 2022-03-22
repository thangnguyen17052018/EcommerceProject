package com.nminhthang.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(FileUploadUtil.USER_DIR_NAME, registry);
        exposeDirectory("../" + FileUploadUtil.CATEGORY_DIR_NAME, registry);
        exposeDirectory("../" + FileUploadUtil.BRAND_DIR_NAME, registry);
        exposeDirectory("../" + FileUploadUtil.PRODUCT_DIR_NAME, registry);
    }

    public void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
        Path path = Paths.get(pathPattern);
        String absolutePath = path.toFile().getAbsolutePath();

        String logicalPath = pathPattern.replace("..", "") + "/**";

        registry.addResourceHandler(logicalPath)
                .addResourceLocations("file:///" + absolutePath + "/");
    }
}
