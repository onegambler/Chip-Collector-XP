package com.chipcollector;

import com.chipcollector.data.configuration.ApplicationProperties;
import com.chipcollector.spring.SpringFxmlLoader;
import com.chipcollector.util.DatabaseUtil;
import com.chipcollector.util.MessagesHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

import static com.avaje.ebean.Ebean.getDefaultServer;

@SpringBootApplication
public class App extends Application {

    private Stage stage;
    @Autowired
    private ApplicationProperties properties;
    @Autowired
    private SpringFxmlLoader loader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        final String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        ConfigurableApplicationContext applicationContext = SpringApplication.run(getClass(), args);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        new DatabaseUtil(getDefaultServer()).tryDatabaseUpdate();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = loader.getStage(DASHBOARD_FX_FILE_LOCATION, MessagesHelper.getString("main.title.text"));
        stage.setWidth(properties.getMainWindowWidth());
        stage.setHeight(properties.getMainWindowHeight());
        stage.setMaximized(properties.isWindowMaximised());
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        properties.setMainWindowHeight(stage.getHeight());
        properties.setMainWindowWidth(stage.getWidth());
        properties.setWindowMaximised(stage.isMaximized());
        Platform.exit();
        System.exit(0);
    }

    public static final String DASHBOARD_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/MainWindow.fxml";
}
