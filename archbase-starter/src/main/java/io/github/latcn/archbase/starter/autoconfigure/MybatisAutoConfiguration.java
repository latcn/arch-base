package io.github.latcn.archbase.starter.autoconfigure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.github.latcn.archbase.data.mybatis.factory.MybatisRepositoryFactory;
import io.github.latcn.archbase.data.mybatis.handler.AutoFillMetaObjectHandler;
import io.github.latcn.archbase.foundation.repository.IRepositoryFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "archbase.mybatis", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnClass(name = "com.baomidou.mybatisplus.core.mapper.BaseMapper")
public class MybatisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IRepositoryFactory repositoryFactory() {
        return new MybatisRepositoryFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler() {
        return new AutoFillMetaObjectHandler();
    }
}