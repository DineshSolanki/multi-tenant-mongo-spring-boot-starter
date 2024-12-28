package io.github.dineshsolanki.multitanent.mongo.config;

import io.github.dineshsolanki.multitanent.mongo.constants.TenantInfoSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "tenant.info")
public class TenantInfoProperties {
    private List<TenantInfoSource> source = List.of(TenantInfoSource.HEADER);
    private String headerName = "X-Tenant-ID";
}
