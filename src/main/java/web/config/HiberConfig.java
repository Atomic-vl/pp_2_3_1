package web.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
@ComponentScan(value = "web")
public class HiberConfig {
    private final Environment env;

    @Autowired
    public HiberConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSource.setUrl(env.getRequiredProperty("db.url"));
        dataSource.setUsername(env.getRequiredProperty("db.username"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
//        properties.put("hibernate.useUnicode", env.getRequiredProperty("hibernate.useUnicode"));
//        properties.put("hibernate.characterEncoding", env.getRequiredProperty("hibernate.characterEncoding"));
//        properties.put("hibernate.charSet", env.getRequiredProperty("hibernate.charSet"));
//        properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        return properties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean entityManagerFB = new LocalContainerEntityManagerFactoryBean();
        entityManagerFB.setDataSource(getDataSource());
        entityManagerFB.setPackagesToScan("web");
        entityManagerFB.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFB.setJpaProperties(hibernateProperties());
        return entityManagerFB;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}