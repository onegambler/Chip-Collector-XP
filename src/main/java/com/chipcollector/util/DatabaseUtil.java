package com.chipcollector.util;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebean.dbmigration.DdlGenerator;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.chipcollector.domain.Property;
import com.google.common.base.Throwables;

import java.io.IOException;

import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.toByteArray;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class DatabaseUtil {
    private static final int DEFAULT_DATABASE_VERSION = 1;

    private final DdlGenerator generator;
    private EbeanServer server;
    private int latestDatabaseVersion;

    public DatabaseUtil(EbeanServer server) {
        this(server, DEFAULT_DATABASE_VERSION);
    }

    DatabaseUtil(EbeanServer server, int latestDatabaseVersion) {
        this.server = server;
        this.latestDatabaseVersion = latestDatabaseVersion;
        final ServerConfig serverConfig = new ServerConfig();
        serverConfig.setDatabasePlatform(new SQLitePlatform());
        generator = new DdlGenerator((SpiEbeanServer) server, serverConfig);
    }

    public void tryDatabaseUpdate() {
        try {
            updateDatabase(latestDatabaseVersion);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    private void updateDatabase(int latestVersion) throws IOException {
        int currentVersion;
        if (databaseExists()) {
            Property databaseVersion = server.createQuery(Property.class).where("key = 'db_version'").findUnique();
            requireNonNull(databaseVersion, "Impossible to load current database version");
            currentVersion = Integer.valueOf(databaseVersion.getValue());
        } else {
            currentVersion = 0;
        }
        for (int i = currentVersion + 1; i <= latestVersion; i++) {
            String updateScript = format("migration/%s.sql", i);
            String databaseUpdateString = new String(toByteArray(getResource(updateScript)));
            generator.runScript(false, databaseUpdateString, updateScript);
        }
    }

    private boolean databaseExists() {
        return server.createSqlQuery("SELECT count(*) FROM sqlite_master").findUnique().getInteger("count(*)") > 0;

    }
}
