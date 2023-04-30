package kz.iitu.diploma_resource_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final DataSource source;
    private final TokenStore tokenStore;

    @Autowired
    public ResourceServerConfig(DataSource source, TokenStore tokenStore) {
        this.source = source;
        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/event/**").permitAll()
                .mvcMatchers("/user/register").permitAll()
                .mvcMatchers("/organization/save-org").permitAll()
                .mvcMatchers("/organization/save-org").permitAll()
                .anyRequest().authenticated()
                .and()
                .cors().disable()
                .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore);
    }

}
