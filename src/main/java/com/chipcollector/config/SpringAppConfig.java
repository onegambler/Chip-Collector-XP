package com.chipcollector.config;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.chipcollector.data.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static java.lang.String.format;

@Configuration
@ComponentScan("com.chipcollector")
public class SpringAppConfig {

    @Autowired
    private AppSettings appSettings;

    @Bean
    public EbeanServer ebeanServer() {
        ServerConfig serverConfig = serverConfig();
        if (appSettings.getLastUsedDatabase().isPresent()) {
            serverConfig.getDataSourceConfig().setUrl(format("jdbc:sqlite:%s", appSettings.getLastUsedDatabase().get()));
        }

        EbeanServer ebeanServer = EbeanServerFactory.create(serverConfig);
        Ebean.register(ebeanServer, true);
        return ebeanServer;
    }

    @Bean
    public ServerConfig serverConfig() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("sqlite");
        serverConfig.loadFromProperties();
        return serverConfig;
    }
}
