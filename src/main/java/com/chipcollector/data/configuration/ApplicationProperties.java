package com.chipcollector.data.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationProperties {

    private final Configuration config;

    @Autowired
    public ApplicationProperties(Configuration configuration) throws ConfigurationException {
        config = configuration;
    }

    public double getMainWindowHeight() {
        return config.getDouble(Preference.WINDOW_HEIGHT.getKeyName());
    }

    public void setMainWindowHeight(double windowHeight) {
        config.setProperty(Preference.WINDOW_HEIGHT.getKeyName(), windowHeight);
    }

    public double getMainWindowWidth() {
        return config.getDouble(Preference.WINDOW_WIDTH.getKeyName());
    }

    public void setMainWindowWidth(double windowWidth) {
        config.setProperty(Preference.WINDOW_WIDTH.getKeyName(), windowWidth);
    }

    public int getPaginationSize() {
        return config.getInt(Preference.PAGINATION_SIZE.getKeyName());
    }

    public void setPaginationSize(int paginationSize) {
        config.setProperty(Preference.PAGINATION_SIZE.getKeyName(), paginationSize);
    }

    public boolean isWindowMaximised() {
        return config.getBoolean(Preference.WINDOW_MAXIMISED.getKeyName());
    }

    public void setWindowMaximised(boolean isMaximised) {
        config.setProperty(Preference.WINDOW_MAXIMISED.getKeyName(), isMaximised);
    }

    private enum Preference {
        WINDOW_HEIGHT("window.size.h"),
        WINDOW_WIDTH("window.size.w"),
        PAGINATION_SIZE("pagination.size"),
        WINDOW_MAXIMISED("window.maximised");

        private final String keyName;

        Preference(String keyName) {
            this.keyName = keyName;
        }

        public String getKeyName() {
            return keyName;
        }
    }
}