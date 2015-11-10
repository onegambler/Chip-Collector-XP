package com.chipcollector.config;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.chipcollector")
public class SpringAppConfig {

    @Bean
    public EbeanServer ebeanServer() {
        return Ebean.getDefaultServer();
    }
}
