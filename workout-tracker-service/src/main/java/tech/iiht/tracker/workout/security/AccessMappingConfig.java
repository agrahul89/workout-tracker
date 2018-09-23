package tech.iiht.tracker.workout.security;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Component
public class AccessMappingConfig extends WebMvcConfigurationSupport {

	@Override // Not of any particular use
	protected void addCorsMappings(CorsRegistry registry) {
		// Allow GET access to all URL
		registry.addMapping("/**").allowedOrigins("*").allowedMethods(HttpMethod.GET.name()).allowedHeaders("*")
				.allowCredentials(true).maxAge(0);

		// Allow POST/OPTIONS access to login
		registry.addMapping("**/login").allowedOrigins("*")
				.allowedMethods(HttpMethod.POST.name(), HttpMethod.OPTIONS.name()).allowedHeaders("*")
				.allowCredentials(true).maxAge(0);
		registry.addMapping("**/login/**").allowedOrigins("*")
				.allowedMethods(HttpMethod.POST.name(), HttpMethod.OPTIONS.name()).allowedHeaders("*")
				.allowCredentials(true).maxAge(0);

		// Allow POST/OPTIONS access to logout
		registry.addMapping("**/logout").allowedOrigins("*")
				.allowedMethods(HttpMethod.POST.name(), HttpMethod.OPTIONS.name()).allowedHeaders("*")
				.allowCredentials(true).maxAge(0);
		registry.addMapping("**/logout/**").allowedOrigins("*")
				.allowedMethods(HttpMethod.POST.name(), HttpMethod.OPTIONS.name()).allowedHeaders("*")
				.allowCredentials(true).maxAge(0);

		// Allow POST/OPTIONS access to registration
		registry.addMapping("**/registration").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
				.allowCredentials(true).maxAge(0);
		registry.addMapping("**/registration/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
				.allowCredentials(true).maxAge(0);

		// Allow POST/OPTIONS access to category
		registry.addMapping("**/category").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
				.allowCredentials(true).maxAge(0);
		registry.addMapping("**/category/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
				.allowCredentials(true).maxAge(0);

		// Allow POST/OPTIONS access to workout
		registry.addMapping("**/workout").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
				.allowCredentials(true).maxAge(0);
		registry.addMapping("**/workout/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
				.allowCredentials(true).maxAge(0);
	}

	@Override
	protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8,
				MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_PLAIN);
	}

	@Override
	protected void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(true).setUseTrailingSlashMatch(true);
	}

}
