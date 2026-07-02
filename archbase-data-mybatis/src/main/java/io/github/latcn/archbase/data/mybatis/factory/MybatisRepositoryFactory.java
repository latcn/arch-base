package io.github.latcn.archbase.data.mybatis.factory;

import io.github.latcn.archbase.foundation.assembler.IAssembler;
import io.github.latcn.archbase.foundation.repository.IRepositoryFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MybatisRepositoryFactory implements IRepositoryFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getMapper(Class<T> mapperClass) {
        return applicationContext.getBean(mapperClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Entity, PO> IAssembler<Entity, PO> getAssembler() {
        return applicationContext.getBean(IAssembler.class);
    }
}