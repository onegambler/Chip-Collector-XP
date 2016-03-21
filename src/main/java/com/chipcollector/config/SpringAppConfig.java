package com.chipcollector.config;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.chipcollector.data.configuration.EbeanConfiguration;
import com.chipcollector.spring.MainProfile;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@MainProfile
@ComponentScan("com.chipcollector")
@org.springframework.context.annotation.Configuration
public class SpringAppConfig {

    private static final String DEFAULT_RESOURCE_FILE_NAME = "app.properties";

    @Bean
    public EbeanServer ebeanServer() {
        ServerConfig serverConfig = serverConfig();

        EbeanServer ebeanServer = EbeanServerFactory.create(serverConfig);
        Ebean.register(ebeanServer, true);
        return ebeanServer;
    }

    @Bean
    public Configuration appConfiguration(EbeanServer ebeanServer) throws ConfigurationException {
        final CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new EbeanConfiguration(ebeanServer), true);
        config.addConfiguration(new PropertiesConfiguration(DEFAULT_RESOURCE_FILE_NAME));
        return config;
    }


    private ServerConfig serverConfig() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("sqlite");
        serverConfig.loadFromProperties();
        return serverConfig;
    }
}
