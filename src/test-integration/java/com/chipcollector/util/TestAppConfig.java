package com.chipcollector.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.chipcollector")
@org.springframework.context.annotation.Configuration
public class TestAppConfig {

    private static final String TEST_RESOURCE_FILE_NAME = "test-app.properties";

    @Bean
    public Configuration appConfiguration() throws ConfigurationException {
        return new PropertiesConfiguration(TEST_RESOURCE_FILE_NAME);
    }
}
