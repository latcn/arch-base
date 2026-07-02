package io.github.latcn.archbase.starter.autoconfigure;

import io.github.latcn.archbase.cqrs.bus.ICommandBus;
import io.github.latcn.archbase.cqrs.bus.IQueryBus;
import io.github.latcn.archbase.cqrs.impl.InMemoryCommandBus;
import io.github.latcn.archbase.cqrs.impl.InMemoryQueryBus;
import io.github.latcn.archbase.cqrs.interceptor.IBusInterceptor;
import io.github.latcn.archbase.cqrs.registry.HandlerRegistry;
import io.github.latcn.archbase.starter.properties.CqrsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "archbase.cqrs", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnClass(name = "io.github.latcn.archbase.cqrs.bus.ICommandBus")
@EnableConfigurationProperties(CqrsProperties.class)
public class CqrsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ICommandBus commandBus(List<IBusInterceptor> interceptors) {
        return new InMemoryCommandBus(interceptors);
    }

    @Bean
    @ConditionalOnMissingBean
    public IQueryBus queryBus(List<IBusInterceptor> interceptors) {
        return new InMemoryQueryBus(interceptors);
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerRegistry handlerRegistry(
            ApplicationContext applicationContext,
            ICommandBus commandBus,
            IQueryBus queryBus) {
        HandlerRegistry registry = new HandlerRegistry(applicationContext, commandBus, queryBus);
        registry.registerAll();
        return registry;
    }
}