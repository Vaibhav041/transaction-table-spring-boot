package com.table.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DataApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataApplication.class, args);
	}

	// allowing cross origin resource sharing
	@Configuration
	public class CorsConfiguration {
		@Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**")
							.allowedOrigins("*") // You can specify specific origins here, e.g., "http://localhost:3000"
							.allowedMethods("GET", "POST", "PUT", "DELETE")
							.allowedHeaders("*");
				}
			};
		}
	}

}
