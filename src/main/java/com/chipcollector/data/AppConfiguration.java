package com.chipcollector.data;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Properties;

public class AppConfiguration {

    private Properties appProperties;

    private static class SingletonHolder {
        public static final AppConfiguration INSTANCE = new AppConfiguration();
    }

    private AppConfiguration() {
        appProperties = new Properties();
        try {
            loadSettings();
        } catch (Exception e) {
            loadDefaultSettings();
        }

    }

    private void loadDefaultSettings() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension s = getMainWindowSize();
        appProperties.setProperty("lastFile", "");
        appProperties.setProperty("lastDirectory", "");
        appProperties.setProperty("xPos", String.valueOf((int) (d.getWidth() - s.getWidth()) / 2));
        appProperties.setProperty("yPos", String.valueOf((int) (d.getHeight() - s.getHeight()) / 2));
        appProperties.setProperty("width", "1000");
        appProperties.setProperty("height", "600");
        appProperties.setProperty("maximized", "false");
        appProperties.setProperty("dateFormat", String.valueOf(DateFormat.MEDIUM));
    }

    public static AppConfiguration getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void loadSettings() throws IOException {
        FileInputStream in = new FileInputStream("appProperties");
        appProperties.load(in);
        in.close();
    }

    public void saveSettings() throws IOException {
        FileOutputStream out = new FileOutputStream("appProperties");
        appProperties.store(out, "---No Comment---");
        out.close();
    }

    public String getLastFile() {
        return appProperties.getProperty("lastFile", "");
    }

    public void setLastFile(String fileName) {
        appProperties.setProperty("lastFile", fileName);
    }

    public String getLastDirectory() {
        return appProperties.getProperty("lastDirectory", "");
    }

    public void setMainWindowPosition(Point location) {
        appProperties.setProperty("xPos", String.valueOf(location.x));
        appProperties.setProperty("yPos", String.valueOf(location.y));
    }

    public Point getMainWindowPosition() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension s = getMainWindowSize();
        int x = Integer.parseInt(appProperties.getProperty("xPos", String.valueOf((int) (d.getWidth() - s.getWidth()) / 2)));
        int y = Integer.parseInt(appProperties.getProperty("yPos", String.valueOf((int) (d.getHeight() - s.getHeight()) / 2)));
        return new Point(x, y);
    }

    public void setMainWindowSize(Dimension size) {
        appProperties.setProperty("width", String.valueOf(size.width));
        appProperties.setProperty("height", String.valueOf(size.height));
    }

    public Dimension getMainWindowSize() {

        int width = Integer.parseInt(appProperties.getProperty("width", "1000"));
        int height = Integer.parseInt(appProperties.getProperty("height", "600"));
        return new Dimension(width, height);
    }

    public void setMaximized(boolean maximized) {
        appProperties.getProperty("maximized", String.valueOf(maximized));
    }

    public boolean isMaximized() {
        return Boolean.parseBoolean(appProperties.getProperty("maximized", "false"));
    }

    public int getVerticalSPDividerLocation() {
        return Integer.parseInt(appProperties.getProperty("vertSPLocation", "10"));
    }

    public int getHoriziontalSPDividerLocation() {
        return Integer.parseInt(appProperties.getProperty("horSPLocation", "10"));
    }

    public void setVerticalSPDividerLocation(int dividerLocation) {
        appProperties.setProperty("vertSPLocation", String.valueOf(dividerLocation));
    }

    public void setHoriziontalSPDividerLocation(int dividerLocation) {
        appProperties.setProperty("horSPLocation", String.valueOf(dividerLocation));
    }

    public int getDateFormat() {
        return Integer.parseInt(appProperties.getProperty("dateFormat", String.valueOf(DateFormat.DEFAULT)));
    }
}