package com.chipcollector.config;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.chipcollector.data.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static java.lang.String.format;

@Configuration
@ComponentScan("com.chipcollector")
public class SpringAppConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public EbeanServer ebeanServer() {
        ServerConfig serverConfig = serverConfig();
        applicationProperties.getLastUsedDatabase()
                .ifPresent(lastUsedDatabase ->
                        serverConfig.getDataSourceConfig().setUrl(format("jdbc:sqlite:%s", lastUsedDatabase)));

        EbeanServer ebeanServer = EbeanServerFactory.create(serverConfig);
        Ebean.register(ebeanServer, true);
        return ebeanServer;
    }


    private ServerConfig serverConfig() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("sqlite");
        serverConfig.loadFromProperties();
        return serverConfig;
    }
}
