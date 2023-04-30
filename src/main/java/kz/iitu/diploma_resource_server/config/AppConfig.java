package kz.iitu.diploma_resource_server.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan("kz.iitu.diploma_resource_server")
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/postgres?currentSchema=diploma")
                .username("postgres")
                .password("5246264425");

        return dataSourceBuilder.build();
    }


}
