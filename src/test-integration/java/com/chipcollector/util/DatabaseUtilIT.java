package com.chipcollector.util;

import com.avaje.ebean.EbeanServer;
import com.chipcollector.DatabaseTestUtil;
import com.chipcollector.domain.Property;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseUtilIT {

    public static final String DB_PROPERTIES = "integration-test-ebean.properties";
    private static final DatabaseTestUtil DATABASE_TEST_UTIL = new DatabaseTestUtil(DB_PROPERTIES);
    private static final EbeanServer TEST_SERVER = DATABASE_TEST_UTIL.getTestServer();
    private DatabaseUtil databaseUtil = new DatabaseUtil(TEST_SERVER);

    @Test
    public void whenDatabaseIsNewThenCreateIt() {
        DATABASE_TEST_UTIL.createSchema();

        List<String> tables = TEST_SERVER.createSqlQuery("SELECT name FROM sqlite_master WHERE type='table'")
                .findSet().stream().map(sqlRow -> sqlRow.getString("name")).collect(Collectors.toList());

        assertThat(tables).containsOnly("sqlite_sequence", "poker_chips",
                "casinos", "countries", "poker_chip_images", "locations", "properties");
        Property databaseVersion = TEST_SERVER.createQuery(Property.class).where().eq("key", "db.version").findUnique();
        assertThat(databaseVersion).isNotNull();
        assertThat(databaseVersion.getValue()).isEqualTo("1");
    }

    @Test
    public void whenDatabaseExistsAndUpToDateThenDoNothing() {
        databaseUtil.tryDatabaseUpdate();
        databaseUtil.tryDatabaseUpdate();
        databaseUtil.tryDatabaseUpdate();

        List<String> tables = TEST_SERVER.createSqlQuery("SELECT name FROM sqlite_master WHERE type='table'")
                .findSet().stream().map(sqlRow -> sqlRow.getString("name")).collect(Collectors.toList());

        assertThat(tables).containsOnly("sqlite_sequence", "poker_chips",
                "casinos", "countries", "poker_chip_images", "locations", "properties");
        Property databaseVersion = TEST_SERVER.createQuery(Property.class).where().eq("key", "db.version").findUnique();
        assertThat(databaseVersion).isNotNull();
        assertThat(databaseVersion.getValue()).isEqualTo("1");
    }
}