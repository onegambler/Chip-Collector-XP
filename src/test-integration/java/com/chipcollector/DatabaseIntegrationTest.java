package com.chipcollector;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.google.common.io.Resources;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class DatabaseIntegrationTest {

    protected static EbeanServer TEST_SERVER;

    @BeforeClass
    public static void setUpDatabase() throws IOException {
        ServerConfig config = new ServerConfig();
        config.setName("sqlite");

        Properties properties = new Properties();
        properties.load(Resources.getResource("test-ebean.properties").openStream());

        config.loadFromProperties(properties);
        config.setDefaultServer(true);
        config.setRegister(true);

        TEST_SERVER = EbeanServerFactory.create(config);
    }

    @AfterClass
    public static void tearDownDatabase() {
        TEST_SERVER.shutdown(true, false);
        final boolean delete = new File("integration-test.db").delete();
        assertThat(delete).isTrue();
    }
}
