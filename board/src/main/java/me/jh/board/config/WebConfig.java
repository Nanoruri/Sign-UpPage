package me.jh.board.config;

import me.jh.core.utils.PathUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String resourceLocation = PathUtils.getResourceLocation();

        
        registry.addResourceHandler("/images/**")
                .addResourceLocations(resourceLocation);
    }
}