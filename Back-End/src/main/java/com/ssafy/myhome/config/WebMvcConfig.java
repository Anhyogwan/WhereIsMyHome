
package com.ssafy.myhome.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;


@Configuration
@EnableAspectJAutoProxy
public class WebMvcConfig implements WebMvcConfigurer {

	//private final List<String> patterns = Arrays.asList("/board/*", "/admin", "/user/list");


	private final String uploadFilePath;

	public WebMvcConfig(@Value("${file.path.upload-files}") String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
//				.allowedOrigins("http://localhost:8080", "http://localhost:8081")
				.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
						HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
						HttpMethod.PATCH.name())
//					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
				.allowedMethods("*")
				.maxAge(1800);
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("upload/**").addResourceLocations("file:///" + uploadFilePath + "/")
				.setCachePeriod(3600).resourceChain(true).addResolver(new PathResourceResolver());
	}
	
}
