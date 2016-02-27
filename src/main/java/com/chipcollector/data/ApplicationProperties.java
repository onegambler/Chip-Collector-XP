package com.chipcollector.data;

import com.chipcollector.App;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.prefs.Preferences;

import static java.util.Optional.ofNullable;

@Repository
public class ApplicationProperties {

    public double getMainWindowHeigh() {
        return Preference.WINDOW_HEIGHT.getDouble();
    }

    public void setMainWindowHeigh(double windowHeight) {
        Preference.WINDOW_HEIGHT.setDouble(windowHeight);
    }

    public double getMainWindowWidth() {
        return Preference.WINDOW_WIDTH.getDouble();
    }

    public void setMainWindowWidth(double windowWidth) {
        Preference.WINDOW_WIDTH.setDouble(windowWidth);
    }

    public Optional<String> getLastUsedDatabase() {
        return ofNullable(Preference.LAST_DATABASE.getString());
    }

    public void setLastUsedDatabase(String path) {
        Preference.LAST_DATABASE.setString(path);
    }

    public int getPaginationSize() {
        return Preference.PAGINATION_SIZE.getInt();
    }

    public void setPaginationSize(int paginationSize) {
        Preference.PAGINATION_SIZE.setInt(paginationSize);
    }

    private enum Preference {
        WINDOW_HEIGHT("window.size.h", 600d),
        WINDOW_WIDTH("window.size.w", 600d),
        LAST_DATABASE("database.path", null),
        PAGINATION_SIZE("pagination.size", 200);

        private final String preferenceName;
        private final Object defaultValue;
        private final Preferences preferences = Preferences.userNodeForPackage(App.class);

        Preference(String preferenceName, Object defaultValue) {
            this.preferenceName = preferenceName;
            this.defaultValue = defaultValue;
        }

        public void setString(String value) {
            preferences.put(preferenceName, value);
        }

        public String getString() {
            return preferences.get(preferenceName, (String) defaultValue);
        }

        public void setDouble(double value) {
            preferences.putDouble(preferenceName, value);
        }

        public double getDouble() {
            return preferences.getDouble(preferenceName, (double) defaultValue);
        }

        public int getInt() {
            return preferences.getInt(preferenceName, (int) defaultValue);
        }

        public void setInt(int value) {
            preferences.putInt(preferenceName, value);
        }
    }
}