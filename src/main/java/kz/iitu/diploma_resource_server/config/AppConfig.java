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
                .url("jdbc:postgresql://ec2-3-217-146-37.compute-1.amazonaws.com:5432/d68cimlnrvdpjo?currentSchema=diploma")
                .username("ywvjmltmyyddhf")
                .password("6c2fe5ef9f20fb3c22c6a3d4ba0ea461f8adacd52d08073aeb3cf5493050bb09");

        return dataSourceBuilder.build();
    }


}
