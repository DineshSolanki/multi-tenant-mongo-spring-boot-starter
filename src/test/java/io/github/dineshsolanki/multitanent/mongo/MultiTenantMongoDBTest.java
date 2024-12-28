package io.github.dineshsolanki.multitanent.mongo;

import io.github.dineshsolanki.multitanent.mongo.config.MongoConfigProperties;
import io.github.dineshsolanki.multitanent.mongo.component.TenantHolder;
import io.github.dineshsolanki.multitanent.mongo.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class MultiTenantMongoDBTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testMultiTenancyEnabled() {
        String dbName = mongoTemplate.getDb().getName();
        assertEquals("tenant2", dbName);
    }

}
