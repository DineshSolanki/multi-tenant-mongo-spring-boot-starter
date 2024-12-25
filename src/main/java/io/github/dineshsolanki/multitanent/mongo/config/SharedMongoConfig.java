package io.github.dineshsolanki.multitanent.mongo.config;

import io.github.dineshsolanki.multitanent.mongo.annotation.SharedCollection;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = SharedCollection.class),
        mongoTemplateRef = "mongoTemplateShared"
)
public class SharedMongoConfig {

}
