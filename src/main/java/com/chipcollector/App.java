package com.chipcollector;

import com.chipcollector.config.SpringAppConfig;
import com.chipcollector.data.configuration.ApplicationProperties;
import com.chipcollector.spring.SpringFxmlLoader;
import com.chipcollector.util.DatabaseUtil;
import com.chipcollector.util.MessagesHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

import static com.avaje.ebean.Ebean.getDefaultServer;

public class App extends Application {

    private Logger logger = LoggerFactory.getLogger(App.class);
    private ApplicationProperties properties;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            new DatabaseUtil(getDefaultServer()).tryDatabaseUpdate();

            final ApplicationContext context = new AnnotationConfigApplicationContext(SpringAppConfig.class);

            properties = context.getBean(ApplicationProperties.class);
            SpringFxmlLoader loader = context.getBean(
                    SpringFxmlLoader.class);

            stage = loader.getStage(DASHBOARD_FX_FILE_LOCATION, MessagesHelper.getString("main.title.text"));
            stage.setWidth(properties.getMainWindowWidth());
            stage.setHeight(properties.getMainWindowHeight());
            stage.setMaximized(properties.isWindowMaximised());
            stage.show();
        } catch (Exception e) {
            logger.error("Impossible to start application", e);
        }
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
