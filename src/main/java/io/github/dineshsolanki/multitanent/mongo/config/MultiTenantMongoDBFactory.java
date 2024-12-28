package io.github.dineshsolanki.multitanent.mongo.config;

import io.github.dineshsolanki.multitanent.mongo.component.TenantHolder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.lang.NonNull;

public class MultiTenantMongoDBFactory extends SimpleMongoClientDatabaseFactory {

    private final String globalDB;

    private final TenantHolder tenantHolder;

    public MultiTenantMongoDBFactory(MongoClient mongoClient, String globalDB, TenantHolder tenantHolder) {
        super(mongoClient, globalDB);
        this.globalDB = globalDB;
        this.tenantHolder = tenantHolder;
    }

    @Override
    @NonNull
    public MongoDatabase getMongoDatabase() {
        return getMongoClient().getDatabase(getTenantDatabase());
    }

    protected String getTenantDatabase() {
        String tenantId = tenantHolder.getTenantId();
        if (tenantId != null) {
            return tenantId;
        } else {
            return globalDB;
        }
    }
}