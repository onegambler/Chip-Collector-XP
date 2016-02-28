package com.chipcollector;

import com.chipcollector.config.SpringAppConfig;
import com.chipcollector.spring.SpringFxmlLoader;
import com.chipcollector.util.DatabaseUtil;
import com.chipcollector.util.MessagesHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

import static com.avaje.ebean.Ebean.getDefaultServer;

public class App extends Application {

    private Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            new DatabaseUtil(getDefaultServer()).tryDatabaseUpdate();

            SpringFxmlLoader loader = new AnnotationConfigApplicationContext(SpringAppConfig.class)
                    .getBean(SpringFxmlLoader.class);
            loader.show(DASHBOARD_FX_FILE_LOCATION, MessagesHelper.getString("main.title.text"));
        } catch (Exception e) {
            logger.error("Impossible to start application", e);
        }
    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
        System.exit(0);
    }

    public static final String DASHBOARD_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/MainWindow.fxml";
}
