package io.github.dineshsolanki.multitanent.mongo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MongoConfigProperties {

    @Value("${spring.data.mongodb.database:global}")
    private String dataBaseName;
}