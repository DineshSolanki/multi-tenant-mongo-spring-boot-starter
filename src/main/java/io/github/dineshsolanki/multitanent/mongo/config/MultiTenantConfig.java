package io.github.dineshsolanki.multitanent.mongo.config;

import io.github.dineshsolanki.multitanent.mongo.interceptor.MultiTenantHandlerInterceptor;
import io.github.dineshsolanki.multitanent.mongo.component.TenantHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MultiTenantConfig implements WebMvcConfigurer {

    private final TenantInfoProperties tenantInfoProperties;

    public MultiTenantConfig(TenantInfoProperties tenantInfoProperties) {
        this.tenantInfoProperties = tenantInfoProperties;
    }

    @Bean
    public TenantHolder tenantHolder() {
        return new TenantHolder();
    }
    @Bean
    public MultiTenantHandlerInterceptor multiTenantHandlerInterceptor(TenantHolder tenantHolder) {
        return new MultiTenantHandlerInterceptor(tenantHolder, tenantInfoProperties.getSource(), tenantInfoProperties.getHeaderName());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(multiTenantHandlerInterceptor(new TenantHolder()));
    }
}