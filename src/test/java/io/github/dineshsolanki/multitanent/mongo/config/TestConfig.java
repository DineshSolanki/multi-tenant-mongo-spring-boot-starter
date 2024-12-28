

package io.github.dineshsolanki.multitanent.mongo.config;

import io.github.dineshsolanki.multitanent.mongo.component.TenantHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;


public class TestConfig {

    @Bean
    public MongoConfigProperties mongoConfigProperties() {
        MongoConfigProperties properties = new MongoConfigProperties();
        properties.setDataBaseName("mongodb://localhost:27017/testdb");
        return properties;
    }

    @Bean
    public TenantHolder tenantHolder() {
        return new TenantHolder();
    }

    @Bean
    public MultiTenantMongoAppConfig multiTenantMongoAppConfig(MongoConfigProperties mongoConfigProperties, TenantHolder tenantHolder) {
        return new MultiTenantMongoAppConfig(mongoConfigProperties, tenantHolder);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoConfigProperties mongoConfigProperties) {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoConfigProperties.getDataBaseName()));
    }
}
