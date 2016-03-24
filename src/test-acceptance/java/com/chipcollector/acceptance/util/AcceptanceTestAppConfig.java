package com.chipcollector.acceptance.util;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.chipcollector.spring.SpringFxmlLoader;
import com.chipcollector.spring.TestProfile;
import com.google.common.io.Resources;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.Properties;

@ComponentScan("com.chipcollector")
@org.springframework.context.annotation.Configuration
public class AcceptanceTestAppConfig {

    private static final String TEST_RESOURCE_FILE_NAME = "acceptance-test-app.properties";

    @Bean
    public Configuration appConfiguration() throws ConfigurationException {
        return new PropertiesConfiguration(TEST_RESOURCE_FILE_NAME);
    }

    @Bean
    public EbeanServer ebeanServer() throws IOException {
        ServerConfig serverConfig = serverConfig();
        Properties properties = new Properties();
        properties.load(Resources.getResource("acceptance-test-ebean.properties").openStream());

        serverConfig.loadFromProperties(properties);
        serverConfig.setDefaultServer(true);
        serverConfig.setRegister(true);

        return EbeanServerFactory.create(serverConfig);
    }


    private ServerConfig serverConfig() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("sqlite");
        serverConfig.loadFromProperties();
        return serverConfig;
    }
}
