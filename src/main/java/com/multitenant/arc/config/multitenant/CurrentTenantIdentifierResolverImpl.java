package com.multitenant.arc.config.multitenant;

import com.multitenant.arc.config.Constants;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentTenantIdentifierResolverImpl.class);

    /**
     * It will resolve current tenant id.
     * if tenant id not set, then it will return default tenant id.
     *
     * */
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getCurrentTenant();
        if(tenantId != null){
            LOGGER.info("Current tenant set to : {}", tenantId);
            return tenantId;
        }
        LOGGER.info("Current tenant set to : {}", Constants.DEFAULT_TENANT);
        return Constants.DEFAULT_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
