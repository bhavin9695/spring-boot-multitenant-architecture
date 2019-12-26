package com.multitenant.arc.config.multitenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TenantContext {
    private static Logger logger = LoggerFactory.getLogger(TenantContext.class);

    private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

    /**
     * This will set tenant value for current thread.
     * */
    public static void setCurrentTenant(String tenant){
        logger.debug("Setting tenant to : {}", tenant);
        currentTenant.set(tenant);
    }

    /**
     * This will return tenant value of current thread.
     * */
    public static String getCurrentTenant(){
        return currentTenant.get();
    }

    /**
     * This will return clear out the tenant value of current thread.
     * */
    public static void clearTenant(){
        currentTenant.remove();
    }
}
