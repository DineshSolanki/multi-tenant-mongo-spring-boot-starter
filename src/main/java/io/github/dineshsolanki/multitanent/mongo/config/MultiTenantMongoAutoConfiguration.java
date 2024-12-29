package io.github.dineshsolanki.multitanent.mongo.config;

import com.mongodb.client.MongoClient;
import io.github.dineshsolanki.multitanent.mongo.component.TenantHolder;
import io.github.dineshsolanki.multitanent.mongo.interceptor.MultiTenantHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@ConditionalOnClass({MongoClient.class})
@EnableConfigurationProperties({MongoConfigProperties.class, TenantInfoProperties.class})
public class MultiTenantMongoAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MultiTenantMongoAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public TenantHolder tenantHolder() {
        logger.info("Creating TenantHolder");
        return new TenantHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public MultiTenantHandlerInterceptor multiTenantHandlerInterceptor(TenantHolder tenantHolder, TenantInfoProperties tenantInfoProperties) {
        logger.info("Creating MultiTenantHandlerInterceptor");
        return new MultiTenantHandlerInterceptor(tenantHolder, tenantInfoProperties.getSource(), tenantInfoProperties.getHeaderName());
    }

    @Configuration
    public static class MultiTenantWebConfig implements WebMvcConfigurer {
        private final MultiTenantHandlerInterceptor multiTenantHandlerInterceptor;

        public MultiTenantWebConfig(MultiTenantHandlerInterceptor multiTenantHandlerInterceptor) {
            this.multiTenantHandlerInterceptor = multiTenantHandlerInterceptor;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(multiTenantHandlerInterceptor);
        }
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate(MongoConfigProperties mongoConfigProperties, MappingMongoConverter converter, MongoClient mongoClient, TenantHolder tenantHolder) {
        String globalDb = mongoConfigProperties.getDataBaseName();
        return new MongoTemplate(new MultiTenantMongoDBFactory(mongoClient,globalDb,tenantHolder), converter);
    }

    @Bean(name = "mongoTemplateShared")
    @NonNull
    public MongoTemplate mongoTemplateShared(MongoConfigProperties mongoConfigProperties, MongoClient mongoClient) {
        logger.info("Creating Shared MongoTemplate");
        MongoDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(mongoClient, mongoConfigProperties.getDataBaseName());
        return new MongoTemplate(mongoDbFactory);
    }
}
