package com.multitenant.arc.config.multitenant;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TenantDataSource tenantDataSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);

    /**
    * It will return default data source.
    * */
    @Override
    protected DataSource selectAnyDataSource() {
        LOGGER.info("Selected default data source");
        return this.dataSource;
    }

    /**
     * It will return datasource based on tenant id.
     * if it will not found datasource based on tenant id, then it will return default datasource.
     *
     * */
    @Override
    protected DataSource selectDataSource(String tenantId){
        DataSource dataSource = tenantDataSource.getDataSource(tenantId);
        if(dataSource == null){
            return selectAnyDataSource();
        } else{
            return dataSource;
        }
    }
}
