package com.chipcollector.util;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.dbmigration.DdlGenerator;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.chipcollector.domain.Property;
import com.google.common.base.Throwables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.toByteArray;
import static java.util.Objects.requireNonNull;

@Component
public class DatabaseUtil {

    private final DdlGenerator generator;
    private final Property databaseVersion;

    @Autowired
    public DatabaseUtil(EbeanServer server) {
        generator = ((SpiEbeanServer) server).getDdlGenerator();
        databaseVersion = server.createQuery(Property.class).where("key = 'db_version'").findUnique();
        requireNonNull(databaseVersion, "Impossible to load current database version");
    }

    public void tryDatabaseUpdate(int latestVersion) {
        try {
            updateDatabase(latestVersion);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    private void updateDatabase(int latestVersion) throws IOException {
        int currentVersion = Optional.ofNullable(databaseVersion).map(property -> Integer.valueOf(property.getValue())).orElse(1);
        for (int i = currentVersion; i <= latestVersion; i++) {
            String updateScript = String.format("sql/%s.sql", i);
            String databaseUpdateString = new String(toByteArray(getResource(updateScript)));
            generator.runScript(false, databaseUpdateString);
        }

    }
}
