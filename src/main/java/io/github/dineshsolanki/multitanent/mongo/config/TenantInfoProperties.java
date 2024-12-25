package io.github.dineshsolanki.multitanent.mongo.config;

import io.github.dineshsolanki.multitanent.mongo.constants.TenantInfoSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "tenant.info")
@Getter @Setter
public class TenantInfoProperties {
    private List<TenantInfoSource> source = List.of(TenantInfoSource.HEADER, TenantInfoSource.QUERYPARAM, TenantInfoSource.PATHVARIABLE);
    private String headerName = "x-tenant-id";
}