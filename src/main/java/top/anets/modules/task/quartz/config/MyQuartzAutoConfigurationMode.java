package top.anets.modules.task.quartz.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import top.anets.support.datasources.Db;

import javax.sql.DataSource;

/**
 * @author ftm
 * @date 2024-04-07 15:06
 */
@Configuration
public class MyQuartzAutoConfigurationMode {
    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Order(1)
    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer(DataSource dataSource) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource quartz = ds.getDataSource(Db.Nodes);
        return schedulerFactoryBean -> {
            schedulerFactoryBean.setDataSource(quartz);
            schedulerFactoryBean.setTransactionManager(new DataSourceTransactionManager(quartz));
        };
    }
}
