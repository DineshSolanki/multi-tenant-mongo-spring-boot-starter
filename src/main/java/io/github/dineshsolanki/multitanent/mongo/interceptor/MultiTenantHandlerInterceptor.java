package io.github.dineshsolanki.multitanent.mongo.interceptor;

import io.github.dineshsolanki.multitanent.mongo.component.TenantHolder;
import io.github.dineshsolanki.multitanent.mongo.constants.TenantInfoSource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public class MultiTenantHandlerInterceptor implements HandlerInterceptor {
    private final TenantHolder tenantHolder;
    private final List<TenantInfoSource> tenantInfoSources;
    private final String headerName;

    public MultiTenantHandlerInterceptor(TenantHolder tenantHolder, List<TenantInfoSource> tenantInfoSources, String headerName) {
        this.tenantHolder = tenantHolder;
        this.tenantInfoSources = tenantInfoSources;
        this.headerName = headerName;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler) throws Exception {
        setTenantId(request, tenantHolder);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler, @Nullable Exception ex) throws Exception {
        tenantHolder.clear();
    }

    private void setTenantId(HttpServletRequest request, TenantHolder tenantHolder) {
        String tenantId = "";

        for (TenantInfoSource source : tenantInfoSources) {
            switch (source) {
                case HEADER:
                    tenantId = request.getHeader(headerName);
                    if (!StringUtils.hasText(tenantId)) {
                        return;
                    }
                    break;
                case QUERYPARAM, PATHVARIABLE:
                    throw new UnsupportedOperationException("Query param not supported yet");
            }
        }
        tenantHolder.setTenantId(tenantId);
    }
}