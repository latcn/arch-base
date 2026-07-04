package io.github.latcn.archbase.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "archbase.mybatis")
public class MybatisProperties {

	private boolean enabled = false;

	private boolean autoFill = true;

}