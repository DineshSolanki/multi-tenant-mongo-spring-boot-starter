package io.github.dineshsolanki.multitanent.mongo.config;

import io.github.dineshsolanki.multitanent.mongo.annotation.SharedCollection;
import io.github.dineshsolanki.multitanent.mongo.component.TenantHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.lang.NonNull;

@EnableMongoRepositories(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = SharedCollection.class))
@Configuration
@Primary
public class MultiTenantMongoAppConfig extends AbstractMongoClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MultiTenantMongoAppConfig.class);
    final TenantHolder tenantHolder;

    private final MongoConfigProperties mongoConfigProperties;


    public MultiTenantMongoAppConfig(MongoConfigProperties mongoConfigProperties, TenantHolder tenantHolder) {
        this.mongoConfigProperties = mongoConfigProperties;
        this.tenantHolder = tenantHolder;
    }

    @Override
    @NonNull
    protected String getDatabaseName() {
        return mongoConfigProperties.getDataBaseName();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    @NonNull
    public MongoDatabaseFactory mongoDbFactory() {
        log.info("Creating MongoDbFactory");
        String globalDb = mongoConfigProperties.getDataBaseName();
        return new MultiTenantMongoDBFactory(mongoClient(), globalDb, tenantHolder);
    }

    @Override
    @Bean
    @ConditionalOnMissingBean
    @NonNull
    public MongoTemplate mongoTemplate(@NonNull MongoDatabaseFactory mongoDbFactory,@NonNull MappingMongoConverter converter) {
        log.info("Creating MongoTemplate");
        return new MongoTemplate(mongoDbFactory, converter);
    }

    public MongoTemplate mongoTemplateShared(MongoConfigProperties mongoConfigProperties) {
        log.info("Creating Shared MongoTemplate");
        MongoDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(mongoClient(), mongoConfigProperties.getDataBaseName());
        return new MongoTemplate(mongoDbFactory);
    }
}