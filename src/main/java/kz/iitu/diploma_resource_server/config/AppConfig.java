package kz.iitu.diploma_resource_server.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.sql.DataSource;

@Configuration
@ComponentScan("kz.iitu.diploma_resource_server")
@EnableAuthorizationServer
@EnableResourceServer
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://ec2-52-2-167-43.compute-1.amazonaws.com:5432/d42k32g8527dp3?currentSchema=diploma")
                .username("kgqnrxuigpqvkf")
                .password("a08dff17ba076482cc2a8953c546c272194c8b7c4a8b01fc3eac120e6df8da6d");

        return dataSourceBuilder.build();
    }


}
