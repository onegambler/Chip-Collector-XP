package com.chipcollector;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.server.core.DefaultServer;
import com.chipcollector.domain.BlobImage;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Location;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.util.DatabaseUtil;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class DatabaseTestUtil {

    private EbeanServer testServer;
    private String databaseConfigFile;

    public DatabaseTestUtil(EbeanServer server, String databaseConfigFile) {
        testServer = server;
        this.databaseConfigFile = databaseConfigFile;
    }

    public DatabaseTestUtil(String databaseConfigFile) {
        this.databaseConfigFile = databaseConfigFile;
        try {
            testServer = setUpDatabase();
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    public EbeanServer setUpDatabase() throws IOException {
        ServerConfig config = new ServerConfig();
        config.setName("sqlite");

        Properties properties = new Properties();
        properties.load(Resources.getResource(databaseConfigFile).openStream());

        config.loadFromProperties(properties);
        config.setDefaultServer(true);
        config.setRegister(true);

        return EbeanServerFactory.create(config);
    }

    public void createSchema() {
        new DatabaseUtil(testServer).tryDatabaseUpdate();
    }

    public boolean tearDownDatabase() {
        testServer.shutdown(true, false);
        return deleteDatabaseFile();
    }

    public boolean deleteDatabaseFile() {
        final String databaseUrl = ((DefaultServer) testServer).getServerConfig()
                .getProperties().getProperty("datasource.sqlite.databaseUrl");
        return new File(databaseUrl.replace("jdbc:sqlite:", "")).delete();
    }

    public EbeanServer getTestServer() {
        return testServer;
    }

    public void cleanDatabase() {
        try {
            testServer.beginTransaction();
            testServer.find(BlobImage.class).findEach(testServer::deletePermanent);
            testServer.find(PokerChip.class).findEach(testServer::deletePermanent);
            testServer.find(Casino.class).findEach(testServer::deletePermanent);
            testServer.find(Location.class).findEach(testServer::deletePermanent);
            testServer.commitTransaction();
        } finally {
            testServer.endTransaction();
        }
    }
}
