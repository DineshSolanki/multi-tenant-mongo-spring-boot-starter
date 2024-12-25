package io.github.dineshsolanki.multitanent.mongo.component;

public class TenantHolder {
    private final ThreadLocal<String> tenantId = new ThreadLocal<>();

    public String getTenantId() {
        return tenantId.get();
    }

    public void setTenantId(final String tenantId) {
        this.tenantId.set(tenantId);
    }

    public void clear() {
        tenantId.remove();
    }
}