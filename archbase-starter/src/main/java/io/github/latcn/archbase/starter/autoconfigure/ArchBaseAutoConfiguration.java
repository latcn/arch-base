package io.github.latcn.archbase.starter.autoconfigure;

import io.github.latcn.archbase.starter.properties.ArchBaseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "archbase", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(ArchBaseProperties.class)
@Import({ CqrsAutoConfiguration.class, MybatisAutoConfiguration.class, WebAutoConfiguration.class })
public class ArchBaseAutoConfiguration {

}