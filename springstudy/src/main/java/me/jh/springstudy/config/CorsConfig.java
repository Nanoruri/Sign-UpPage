package me.jh.springstudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;


/**
 * CORS 설정 클래스.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration config = new CorsConfiguration();
//
//		config.setAllowCredentials(true);
//		config.setAllowedOrigins(List.of("http://localhost:8082"));
//		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//		config.setAllowedHeaders(List.of("*"));
//		config.setExposedHeaders(List.of("*"));
//
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", config);
//		return source;
//	}
//
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/js/**")
//				.addResourceLocations("classpath:/static/js/")
//				.resourceChain(true)
//				.addResolver(new PathResourceResolver() {
//					@Override
//					protected org.springframework.core.io.Resource getResource(String resourcePath, org.springframework.core.io.Resource location) throws IOException {
//						org.springframework.core.io.Resource requestedResource = location.createRelative(resourcePath);
//						return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : null;
//					}
//				});
//	}

}