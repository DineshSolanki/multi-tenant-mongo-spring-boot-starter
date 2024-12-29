package io.github.dineshsolanki.multitanent.mongo.interceptor;

import io.github.dineshsolanki.multitanent.mongo.component.TenantHolder;
import io.github.dineshsolanki.multitanent.mongo.constants.TenantInfoSource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * Interceptor for handling multi-tenancy by setting tenant ID from the request.
 */
public class MultiTenantHandlerInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(MultiTenantHandlerInterceptor.class);

    private final TenantHolder tenantHolder;
    private final List<TenantInfoSource> tenantInfoSources;
    private final String headerName;

    public MultiTenantHandlerInterceptor(TenantHolder tenantHolder, List<TenantInfoSource> tenantInfoSources, String headerName) {
        this.tenantHolder = tenantHolder;
        this.tenantInfoSources = tenantInfoSources;
        this.headerName = headerName;
    }

    /**
     * Pre-handle method to set the tenant ID before the request is processed.
     * @param request the HTTP request
     * @param response the HTTP response
     * @param handler the handler
     * @return true to continue processing, false to abort
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler) {
        setTenantId(request, tenantHolder);
        return true;
    }


    /**
     * After completion method to clear the tenant ID after request processing.
     * @param request the HTTP request
     * @param response the HTTP response
     * @param handler the handler
     * @param ex any exception thrown during processing
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler, @Nullable Exception ex) {
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
                    logger.warn("Query param or path variable source not supported for tenant ID retrieval");
                    throw new UnsupportedOperationException("Query param not supported yet");
            }
        }
        logger.info("Setting tenant ID from header: {}", tenantId);
        tenantHolder.setTenantId(tenantId);
    }
}