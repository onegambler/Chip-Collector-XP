package com.chipcollector.util;

import com.chipcollector.DatabaseIntegrationTest;
import com.chipcollector.domain.Property;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseUtilIT extends DatabaseIntegrationTest {

    private DatabaseUtil databaseUtil = new DatabaseUtil(TEST_SERVER);

    @Test
    public void whenDatabaseIsNewThenCreateIt() {
        databaseUtil.tryDatabaseUpdate();

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