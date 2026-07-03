package io.github.latcn.archbase.starter.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.latcn.archbase.web.spring.exception.DefaultExceptionHandler;
import io.github.latcn.archbase.web.spring.exception.GlobalExceptionHandler;
import io.github.latcn.archbase.web.spring.resolver.RequestParamResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "archbase.web", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnClass(name = "org.springframework.web.bind.annotation.ControllerAdvice")
public class WebAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	public GlobalExceptionHandler globalExceptionHandler() {
		return new DefaultExceptionHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public RequestParamResolver requestParamResolver(ObjectMapper objectMapper) {
		return new RequestParamResolver(objectMapper);
	}

}