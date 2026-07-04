package io.github.latcn.archbase.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "archbase.web")
public class WebProperties {

	private boolean enabled = false;

	private String exceptionHandler = "default";

}